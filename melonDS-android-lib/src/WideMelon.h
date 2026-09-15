// WideMelon renderer profile (Android port).
// Based on https://github.com/pruefsumme/widemelon
// SPDX-License-Identifier: GPL-3.0-or-later
#pragma once

#include <cstdint>

namespace WideMelon
{

// View width is fixed for an emulation session so geometry and GL allocations agree.
// Call SetWidth() before the OpenGL renderer is created/resized.
inline int& WidthStorage()
{
    static int width = 256;
    return width;
}

inline void SetWidth(int width)
{
    if (width < 256 || width > 768 || (width % 2) != 0)
        width = 256;
    WidthStorage() = width;
}

inline int Width()
{
    return WidthStorage();
}

inline bool Enabled()
{
    return Width() > 256;
}

inline int NativeWidth()
{
    return 256;
}

inline int SidePad()
{
    return (Width() - NativeWidth()) / 2;
}

// Keep original world scale and center; wider raster reveals extra columns.
inline int32_t ProjectX(int32_t x)
{
    return static_cast<int32_t>(static_cast<int64_t>(x) * NativeWidth() / Width());
}

}
