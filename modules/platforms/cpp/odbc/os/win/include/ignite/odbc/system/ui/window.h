/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef _IGNITE_ODBC_SYSTEM_UI_WINDOW
#define _IGNITE_ODBC_SYSTEM_UI_WINDOW

#include "ignite/odbc/utility.h"
#include "ignite/odbc/system/odbc_constants.h"

namespace ignite
{
    namespace odbc
    {
        namespace system
        {
            namespace ui
            {
                /**
                 * Get handle for the current module.
                 *
                 * @return Handle for the current module.
                 */
                HINSTANCE GetHInstance();

                /**
                 * Window class.
                 */
                class Window
                {
                public:
                    /**
                     * Constructor for a new window that is going to be created.
                     *
                     * @param parent Parent window handle.
                     * @param className Window class name.
                     * @param title Window title.
                     * @param callback Event processing function.
                     */
                    Window(Window* parent, const char* className, const char* title);

                    /**
                     * Constructor for the existing window.
                     *
                     * @param handle Window handle.
                     */
                    Window(HWND handle);

                    /**
                     * Destructor.
                     */
                    virtual ~Window();

                    /**
                     * Create window.
                     *
                     * @param style Window style.
                     * @param posX Window x position.
                     * @param posY Window y position.
                     * @param width Window width.
                     * @param height Window height.
                     * @param id ID for child window.
                     */
                    void Create(DWORD style, int posX, int posY, int width, int height, int id);

                    /**
                     * Show window.
                     */
                    void Show();

                    /**
                     * Update window.
                     */
                    void Update();

                    /**
                     * Destroy window.
                     */
                    void Destroy();

                    /**
                     * Get window handle.
                     *
                     * @return Window handle.
                     */
                    HWND GetHandle() const
                    {
                        return handle;
                    }

                    /**
                     * Get window text.
                     *
                     * @param text Text.
                     */
                    void GetText(std::string& text) const;

                    /**
                     * Set window text.
                     *
                     * @param text Text.
                     */
                    void SetText(const std::string& text) const;

                    /**
                     * Get CheckBox state.
                     *
                     * @param True if checked.
                     */
                    bool IsChecked() const;

                    /**
                     * Set CheckBox state.
                     *
                     * @param state True if checked.
                     */
                    void SetChecked(bool state);

                    /**
                     * Add string.
                     *
                     * @param str String.
                     */
                    void AddString(const std::string& str);

                    /**
                     * Set current ComboBox selection.
                     *
                     * @param idx List index.
                     */
                    void SetSelection(int idx);

                    /**
                     * Get current ComboBox selection.
                     *
                     * @return idx List index.
                     */
                    int GetSelection() const;

                    /**
                     * Set enabled.
                     *
                     * @param enabled Enable flag.
                     */
                    void SetEnabled(bool enabled);

                    /**
                     * Check if the window is enabled.
                     *
                     * @return True if enabled.
                     */
                    bool IsEnabled() const;

                protected:
                    /**
                     * Set window handle.
                     *
                     * @param value Window handle.
                     */
                    void SetHandle(HWND value)
                    {
                        handle = value;
                    }

                    /** Window class name. */
                    std::string className;

                    /** Window title. */
                    std::string title;

                    /** Window handle. */
                    HWND handle;

                    /** Specifies whether window has been created by the thread and needs destruction. */
                    bool created;

                    /** Window parent. */
                    Window* parent;

                private:
                    IGNITE_NO_COPY_ASSIGNMENT(Window)
                };
            }
        }
    }
}

#endif //_IGNITE_ODBC_SYSTEM_UI_WINDOW