/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.owl.chatstory.common.util;


import com.owl.chatstory.common.util.schedulers.BaseSchedulerProvider;
import com.owl.chatstory.common.util.schedulers.SchedulerProvider;

/**
 * Enables injection of mock implementations for
 * at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a scan_arc_rotate_view_layout hermetically.
 */
public class Injection {

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
