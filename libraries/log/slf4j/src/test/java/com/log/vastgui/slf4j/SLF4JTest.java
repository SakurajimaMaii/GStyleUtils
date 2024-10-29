/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.log.vastgui.slf4j;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SLF4JTest {

    private static final String TAG = "SLF4JTest";
    private static final String HELLO_WORLD_EXCEPTION = "Hello World Exception.";
    private static final String HELLO_WORLD = "Hello World";
    private static final String HELLO_WORLD_FORMAT_1 = "Print 1#{}.";
    private static final String HELLO_WORLD_FORMAT_2 = "Print 1#{} 2#{}.";
    private static final String HELLO_WORLD_FORMAT_MORE = "Print 1#{} 2#{} 3#{}.";

    @Test
    public void usage() {
        Slf4jProvider.Options.setFactory(FactoryKt.getLogFactory());
        Logger logger = LoggerFactory.getLogger(TAG);
        logger.debug(MarkerFactory.getMarker("Hello"), "testLogger");
    }

    @Test
    public void dSimpleUsage() {
        Slf4jProvider.Options.setFactory(FactoryKt.getLogFactory());
        Logger logger = LoggerFactory.getLogger(SLF4JTest.class);
        Marker marker = MarkerFactory.getMarker("Marker");
        logger.debug(marker, HELLO_WORLD);
        logger.debug(marker, HELLO_WORLD_FORMAT_1, HELLO_WORLD);
        logger.debug(marker, HELLO_WORLD_FORMAT_2, HELLO_WORLD, HELLO_WORLD);
        logger.debug(marker, HELLO_WORLD_FORMAT_MORE, HELLO_WORLD, HELLO_WORLD, HELLO_WORLD);
        logger.debug(marker, null, new Exception(HELLO_WORLD_EXCEPTION));
        logger.debug(HELLO_WORLD);
        logger.debug(HELLO_WORLD_FORMAT_1, HELLO_WORLD);
        logger.debug(HELLO_WORLD_FORMAT_2, HELLO_WORLD, HELLO_WORLD);
        logger.debug(HELLO_WORLD_FORMAT_MORE, HELLO_WORLD, HELLO_WORLD, HELLO_WORLD);
        logger.debug(HELLO_WORLD, new Exception(HELLO_WORLD_EXCEPTION));
    }

    @Test
    public void builderUsage() {
        Slf4jProvider.Options.setFactory(FactoryKt.getLogFactory());
        Logger logger = LoggerFactory.getLogger("SLF4JTest");

        // region void log(String message)
        if(false) {
            logger.atDebug().log((String) null);
            logger.atDebug().log(HELLO_WORLD);
        }
        // endregion

        // region void log(String message, Object arg)
        if(false) {
            logger.atDebug().log(null, (Object) null);
            logger.atDebug().log(HELLO_WORLD_FORMAT_1, (Object) null);
            logger.atDebug().log(HELLO_WORLD_FORMAT_1, HELLO_WORLD);
            logger.atDebug().log(null, new Exception(HELLO_WORLD_EXCEPTION));
            logger.atDebug().log(HELLO_WORLD, new Exception(HELLO_WORLD_EXCEPTION));
        }
        // endregion

        // region void log(String message, Object arg0, Object arg1)
        if(false) {
            logger.atDebug().log(null, null, null);
            logger.atDebug().log(HELLO_WORLD_FORMAT_1, null, new Exception(HELLO_WORLD_EXCEPTION));
            logger.atDebug().log(HELLO_WORLD_FORMAT_2, null, null);
            logger.atDebug().log(HELLO_WORLD_FORMAT_2, HELLO_WORLD, null);
            logger.atDebug().log(HELLO_WORLD_FORMAT_2, HELLO_WORLD, HELLO_WORLD);
        }
        // endregion

        // region log(String message, Object... args)
        logger.atDebug().log(HELLO_WORLD_FORMAT_MORE, HELLO_WORLD, HELLO_WORLD, HELLO_WORLD);
        logger.atDebug().log(HELLO_WORLD_FORMAT_2, HELLO_WORLD, HELLO_WORLD, new Exception(HELLO_WORLD_EXCEPTION));
        // endregion

        // region log(Supplier<String> messageSupplier)
        if(false) {
            logger.atDebug().log(() -> HELLO_WORLD);
            logger.atDebug().log(() ->
                    """
                            {
                                        "minInt": -9007199254740991,
                                                    "maxInt": 9007199254740991,
                                        "minFloat": -1.7976931348623157e+308,
                                    "maxFloat": 1.7976931348623157e+308
                                    }
                            """
            );
        }
        // endregion
    }

    @Test
    public void errorCase() {
        Slf4jProvider.Options.setFactory(FactoryKt.getLogFactory());
        Logger logger = LoggerFactory.getLogger("SLF4JTest");
        // FIX https://github.com/SakurajimaMaii/Android-Vast-Extension/commit/0cd54593e5416a32ad3fa506725ebdcc8988c113
        logger.atDebug().log("null 123456");
    }

}