package me.magnum.melonds.ui.settings.fragments

import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import me.magnum.melonds.R
import me.magnum.melonds.common.DirectoryAccessValidator
import me.magnum.melonds.common.UriPermissionManager
import me.magnum.melonds.domain.model.VideoFiltering
import me.magnum.melonds.domain.model.VideoRenderer
import me.magnum.melonds.domain.model.WideMelonVideoTuning
import me.magnum.melonds.domain.model.camera.DSiCameraSourceType
import me.magnum.melonds.domain.repositories.LayoutsRepository
import me.magnum.melonds.domain.repositories.SettingsRepository
import me.magnum.melonds.ui.settings.PreferenceFragmentHelper
import me.magnum.melonds.ui.settings.PreferenceFragmentTitleProvider
import me.magnum.melonds.ui.settings.preferences.StoragePickerPreference
import me.magnum.melonds.utils.enumValueOfIgnoreCase
import javax.inject.Inject

@AndroidEntryPoint
class VideoPreferencesFragment : BasePreferenceFragment(), PreferenceFragmentTitleProvider {

    private companion object {
        const val GLES_3_2 = 0x30002
    }

    private val helper by lazy { PreferenceFragmentHelper(this, uriPermissionManager, directoryAccessValidator) }
    @Inject lateinit var uriPermissionManager: UriPermissionManager
    @Inject lateinit var directoryAccessValidator: DirectoryAccessValidator
    @Inject lateinit var layoutsRepository: LayoutsRepository
    @Inject lateinit var settingsRepository: SettingsRepository

    private val softwareRendererPreferences = mutableListOf<Preference>()
    private val openGlRendererPreferences = mutableListOf<Preference>()
    private val computeRendererPreferences = mutableListOf<Preference>()

    private var isAdrenoGpu: Boolean = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_video, rootKey)

        softwareRendererPreferences.apply {
            add(findPreference("enable_threaded_rendering")!!)
        }

        openGlRendererPreferences.apply {
            add(findPreference("video_internal_resolution")!!)
            add(findPreference("video_widescreen_view_width")!!)
        }

        computeRendererPreferences.apply {
            add(findPreference("video_internal_resolution")!!)
            add(findPreference("video_widescreen_view_width")!!)
        }

        val rendererPreference = findPreference<ListPreference>("video_renderer")!!
        val widthPreference = findPreference<ListPreference>("video_widescreen_view_width")!!
        val resolutionPreference = findPreference<ListPreference>("video_internal_resolution")!!
        val filteringPreference = findPreference<ListPreference>("video_filtering")!!
        val presetPreference = findPreference<ListPreference>("video_widescreen_preset")!!
        val costPreference = findPreference<Preference>("video_widescreen_cost_hint")!!
        val dsiCameraSourcePreference = findPreference<ListPreference>("dsi_camera_source")!!
        val dsiCameraImagePreference = findPreference<StoragePickerPreference>("dsi_camera_static_image")!!

        val activityManager = requireContext().getSystemService<ActivityManager>()

        rendererPreference.apply {
            val deviceGlesVersion = activityManager?.deviceConfigurationInfo?.reqGlEsVersion ?: 0
            if (deviceGlesVersion >= GLES_3_2) {
                isAdrenoGpu = Build.HARDWARE.lowercase() == "qcom"
                if (!isAdrenoGpu) {
                    val computeRenderOptionIndex = rendererPreference.entryValues.indexOfFirst { it == VideoRenderer.COMPUTE.name.lowercase() }
                    rendererPreference.entries = rendererPreference.entries.filterIndexed { index, _ ->
                        index != computeRenderOptionIndex
                    }.toTypedArray()
                    rendererPreference.entryValues = rendererPreference.entryValues.filterIndexed { index, _ ->
                        index != computeRenderOptionIndex
                    }.toTypedArray()
                }

                setOnPreferenceChangeListener { _, newValue ->
                    onRendererPreferenceChanged(newValue as String)
                    updateCostHint(costPreference, widthPreference, resolutionPreference, filteringPreference)
                    true
                }
            } else {
                isVisible = false
            }
        }

        widthPreference.setOnPreferenceChangeListener { _, newValue ->
            updateCostHint(costPreference, widthPreference, resolutionPreference, filteringPreference, widthOverride = newValue as String)
            maybeWarnHeavyFilter(filteringPreference, widthOverride = newValue)
            true
        }
        resolutionPreference.setOnPreferenceChangeListener { _, newValue ->
            updateCostHint(costPreference, widthPreference, resolutionPreference, filteringPreference, scaleOverride = newValue as String)
            maybeWarnHeavyFilter(filteringPreference, scaleOverride = newValue)
            true
        }
        filteringPreference.setOnPreferenceChangeListener { _, newValue ->
            val requested = enumValueOfIgnoreCase<VideoFiltering>(newValue as String)
            val width = widthPreference.value?.toIntOrNull() ?: 384
            val scale = resolutionPreference.value?.toIntOrNull() ?: 2
            val adjusted = WideMelonVideoTuning.preferLighterFilter(requested, width, scale)
            if (adjusted != requested) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.widescreen_heavy_filter_downgraded, requested.name, adjusted.name),
                    Toast.LENGTH_LONG,
                ).show()
                filteringPreference.value = adjusted.name.lowercase()
                updateCostHint(costPreference, widthPreference, resolutionPreference, filteringPreference)
                false
            } else {
                updateCostHint(costPreference, widthPreference, resolutionPreference, filteringPreference, filterOverride = newValue)
                true
            }
        }

        presetPreference.setOnPreferenceChangeListener { _, newValue ->
            applyPreset(newValue as String, rendererPreference, widthPreference, resolutionPreference, filteringPreference)
            updateCostHint(costPreference, widthPreference, resolutionPreference, filteringPreference)
            onRendererPreferenceChanged(rendererPreference.value)
            true
        }

        dsiCameraSourcePreference.setOnPreferenceChangeListener { _, newValue ->
            updateDsiCameraImagePreference(dsiCameraImagePreference, newValue as String)
            true
        }

        helper.setupStoragePickerPreference(dsiCameraImagePreference)

        onRendererPreferenceChanged(rendererPreference.value)
        updateDsiCameraImagePreference(dsiCameraImagePreference, dsiCameraSourcePreference.value)
        updateCostHint(costPreference, widthPreference, resolutionPreference, filteringPreference)
    }

    private fun applyPreset(
        presetKey: String,
        rendererPreference: ListPreference,
        widthPreference: ListPreference,
        resolutionPreference: ListPreference,
        filteringPreference: ListPreference,
    ) {
        val preset = WideMelonVideoTuning.Preset.fromKey(presetKey)
        val values = WideMelonVideoTuning.presetValues(preset)
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val rendererValue = when {
            values.rendererHint == "compute" && isAdrenoGpu &&
                rendererPreference.entryValues.any { it == "compute" } -> "compute"
            rendererPreference.entryValues.any { it == "opengl" } -> "opengl"
            else -> rendererPreference.value
        }

        prefs.edit {
            putString("video_renderer", rendererValue)
            putString("video_widescreen_view_width", values.width.toString())
            putString("video_internal_resolution", values.scale.toString())
            putString("video_filtering", values.filtering)
        }
        rendererPreference.value = rendererValue
        widthPreference.value = values.width.toString()
        resolutionPreference.value = values.scale.toString()
        filteringPreference.value = values.filtering

        Toast.makeText(
            requireContext(),
            getString(R.string.widescreen_preset_applied, presetPreferenceLabel(preset)),
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun presetPreferenceLabel(preset: WideMelonVideoTuning.Preset): String = when (preset) {
        WideMelonVideoTuning.Preset.PERFORMANCE -> getString(R.string.widescreen_preset_performance)
        WideMelonVideoTuning.Preset.BALANCED -> getString(R.string.widescreen_preset_balanced)
        WideMelonVideoTuning.Preset.QUALITY -> getString(R.string.widescreen_preset_quality)
        WideMelonVideoTuning.Preset.MAX_WIDE -> getString(R.string.widescreen_preset_max_wide)
    }

    private fun maybeWarnHeavyFilter(
        filteringPreference: ListPreference,
        widthOverride: String? = null,
        scaleOverride: String? = null,
    ) {
        val filtering = runCatching {
            enumValueOfIgnoreCase<VideoFiltering>(filteringPreference.value ?: "vibrant")
        }.getOrDefault(VideoFiltering.VIBRANT)
        if (!WideMelonVideoTuning.isHeavyFilter(filtering)) return

        val width = (widthOverride ?: filteringPreference.sharedPreferences?.getString("video_widescreen_view_width", "384"))
            ?.toIntOrNull() ?: 384
        val scale = (scaleOverride ?: filteringPreference.sharedPreferences?.getString("video_internal_resolution", "2"))
            ?.toIntOrNull() ?: 2
        if (WideMelonVideoTuning.horizontalCost(width, scale) > WideMelonVideoTuning.HEAVY_FILTER_COST_THRESHOLD) {
            Toast.makeText(requireContext(), R.string.widescreen_heavy_filter_warning, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateCostHint(
        costPreference: Preference,
        widthPreference: ListPreference,
        resolutionPreference: ListPreference,
        filteringPreference: ListPreference,
        widthOverride: String? = null,
        scaleOverride: String? = null,
        filterOverride: String? = null,
    ) {
        val width = (widthOverride ?: widthPreference.value)?.toIntOrNull() ?: 384
        val scale = (scaleOverride ?: resolutionPreference.value)?.toIntOrNull() ?: 2
        val filterName = filterOverride ?: filteringPreference.value ?: "vibrant"
        val filtering = runCatching { enumValueOfIgnoreCase<VideoFiltering>(filterName) }
            .getOrDefault(VideoFiltering.VIBRANT)
        val costLine = WideMelonVideoTuning.costLabel(width, scale)
        val filterNote = if (
            WideMelonVideoTuning.isHeavyFilter(filtering) &&
            WideMelonVideoTuning.horizontalCost(width, scale) > WideMelonVideoTuning.HEAVY_FILTER_COST_THRESHOLD
        ) {
            "\n" + getString(R.string.widescreen_heavy_filter_warning)
        } else {
            ""
        }
        costPreference.summary = getString(R.string.widescreen_gpu_cost_summary, costLine) + filterNote
    }

    private fun onRendererPreferenceChanged(rendererValue: String) {
        val newRenderer = enumValueOfIgnoreCase<VideoRenderer>(rendererValue)
        when (newRenderer) {
            VideoRenderer.SOFTWARE -> {
                computeRendererPreferences.forEach { it.isVisible = false }
                openGlRendererPreferences.forEach { it.isVisible = false }
                softwareRendererPreferences.forEach { it.isVisible = true }
            }
            VideoRenderer.OPENGL -> {
                softwareRendererPreferences.forEach { it.isVisible = false }
                computeRendererPreferences.forEach { it.isVisible = false }
                openGlRendererPreferences.forEach { it.isVisible = true }
            }
            VideoRenderer.COMPUTE -> {
                softwareRendererPreferences.forEach { it.isVisible = false }
                openGlRendererPreferences.forEach { it.isVisible = false }
                computeRendererPreferences.forEach { it.isVisible = true }
            }
        }
    }

    private fun updateDsiCameraImagePreference(preference: StoragePickerPreference, dsiCameraSourceValue: String) {
        val newSource = enumValueOfIgnoreCase<DSiCameraSourceType>(dsiCameraSourceValue)
        preference.isEnabled = newSource == DSiCameraSourceType.STATIC_IMAGE
    }

    override fun getTitle(): String {
        return getString(R.string.category_video)
    }
}
