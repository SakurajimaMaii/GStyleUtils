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

package com.log.vastgui.slf4j

import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.NotNullOrDefault
import com.log.vastgui.core.LogFactory
import com.log.vastgui.core.annotation.LogExperimental
import com.log.vastgui.core.LogCat
import org.slf4j.ILoggerFactory
import org.slf4j.IMarkerFactory
import org.slf4j.LoggerFactory
import org.slf4j.helpers.BasicMarkerFactory
import org.slf4j.helpers.NOPMDCAdapter
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/7
// Documentation: https://ave.entropy2020.cn/documents/log/log-slf4j/usage/

/**
 * [SLF4JServiceProvider] based implementation of [LogCat].
 *
 * @since 1.3.7
 */
@LogExperimental
class Slf4jProvider internal constructor() : SLF4JServiceProvider {
    /** @since 1.3.7 */
    private var slf4jFactory: ILoggerFactory by Delegates.notNull()

    /** @since 1.3.7 */
    private var slf4jMarkerFactory: IMarkerFactory by Delegates.notNull()

    /** @since 1.3.7 */
    private var slf4jMdcAdapter: MDCAdapter by Delegates.notNull()

    /** @since 1.3.7 */
    override fun getLoggerFactory() = slf4jFactory

    /** @since 1.3.7 */
    override fun getMarkerFactory() = slf4jMarkerFactory

    /** @since 1.3.7 */
    override fun getMDCAdapter() = slf4jMdcAdapter

    /** @since 1.3.7 */
    override fun getRequestedApiVersion() = REQUESTED_API_VERSION

    /** @since 1.3.7 */
    override fun initialize() {
        slf4jFactory = Options.slf4jFactory
        slf4jMarkerFactory = Options.slf4jMarkerFactory
        slf4jMdcAdapter = Options.slf4jMDCAdapter
    }

    /**
     * Options of [Slf4jProvider]. This must be set before calling
     * [LoggerFactory.getLogger].
     *
     * ```kotlin
     * @LogExperimental
     * class SLF4JKtTest {
     *
     *     private var logger: Logger by Delegates.notNull()
     *
     *     @Test
     *     fun simpleTest() {
     *         Slf4jProvider.Options.setFactory(logFactory)
     *         logger = LoggerFactory.getLogger("SLF4JKtTest")
     *         logger.debug("This is a message")
     *     }
     *
     * }
     * ```
     *
     * @since 1.3.7
     */
    object Options {

        /** @since 1.3.7 */
        internal var slf4jFactory: Slf4jFactory by NotNUllVar(true)

        /** @since 1.3.7 */
        internal var slf4jMDCAdapter: MDCAdapter by NotNullOrDefault(NOPMDCAdapter())

        /** @since 1.3.7 */
        internal var slf4jMarkerFactory: IMarkerFactory by NotNullOrDefault(BasicMarkerFactory())

        /**
         * Set the [ILoggerFactory] of the [Slf4jProvider].
         *
         * @since 1.3.7
         */
        @JvmStatic
        fun setFactory(logFactory: LogFactory) {
            slf4jFactory = Slf4jFactory(logFactory)
        }

        /**
         * Set the [MDCAdapter] of the [Slf4jProvider].
         *
         * @since 1.3.7
         */
        @JvmStatic
        fun setMDCAdapter(mdcAdapter: MDCAdapter) {
            slf4jMDCAdapter = mdcAdapter
        }

        /**
         * Set the [IMarkerFactory] of the [Slf4jProvider].
         *
         * @since 1.3.7
         */
        @JvmStatic
        fun setMarkerFactory(markerFactory: IMarkerFactory) {
            slf4jMarkerFactory = markerFactory
        }

    }

    companion object {
        /** @since 1.3.7 */
        const val REQUESTED_API_VERSION: String = "2.0.16"
    }
}