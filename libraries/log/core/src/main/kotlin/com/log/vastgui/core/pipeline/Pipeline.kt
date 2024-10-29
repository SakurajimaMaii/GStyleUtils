/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.log.vastgui.core.pipeline

import com.ave.vastgui.core.extension.nothing_to_do

// Author: ywnkm
// Email: https://github.com/ywnkm
// Date: 2024/6/22
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/advanced/advanced/
// Reference: https://github.com/ktorio/ktor/blob/861828fee0b6c4b8056036b5bd8f371e26ee5577/ktor-utils/common/src/io/ktor/util/pipeline/Pipeline.kt

/** @since 1.3.4 */
typealias PipelineInterceptor<TSubject, TContext> = PipelineContext<TSubject, TContext>.(TSubject) -> Unit

/** @since 1.3.4 */
open class Pipeline<TSubject : Any, TContext : Any>(vararg phases: PipelinePhase) {

    /**
     * Contains a list of [PipelinePhase] and [PhaseContent].
     *
     * @since 1.3.4
     */
    private val phasesRaw: MutableList<Any> = mutableListOf(*phases)

    /** @since 1.3.4 */
    private var interceptorsQuantity = 0

    /** @since 1.3.4 */
    val items: List<PipelinePhase>
        get() = phasesRaw.map {
            it as? PipelinePhase ?: (it as PhaseContent<*, *>).phase
        }

    /**
     * @return `true` if there are no interceptors installed regardless number
     * of phases.
     * @since 1.3.4
     */
    val isEmpty: Boolean
        get() = interceptorsQuantity == 0

    /**
     * Executes this pipeline in the given [context] and with the given
     * [subject].
     *
     * @since 1.3.4
     */
    internal fun execute(context: TContext, subject: TSubject): TSubject {
        val pipelineContext = createContext(context, subject)
        return pipelineContext.execute(subject)
    }

    /**
     * Add [phase] to the end of [phasesRaw].
     *
     * @since 1.3.4
     */
    fun addPhase(phase: PipelinePhase) {
        if (hasPhase(phase)) return
        phasesRaw.add(phase)
    }

    /**
     * Insert [phase] as [PhaseContent] after the [reference] phase. If there
     * are other phases inserted after [reference], then [phase] will be
     * inserted after them.
     *
     * @since 1.3.4
     */
    fun insertPhaseAfter(reference: PipelinePhase, phase: PipelinePhase) {
        if (hasPhase(phase)) return
        val index = findPhaseIndex(reference)
        if (index == -1) {
            throw IllegalArgumentException("Phase $reference not registered for this pipeline")
        }
        var lastRelatedPhaseIndex = index
        for (i in index + 1..phasesRaw.lastIndex) {
            val relation = (phasesRaw[i] as? PhaseContent<*, *>)?.relation ?: continue
            val relatedTo = (relation as? PipelinePhaseRelation.After)?.relativeTo ?: continue
            lastRelatedPhaseIndex = if (relatedTo == reference) i else lastRelatedPhaseIndex
        }
        phasesRaw.add(
            lastRelatedPhaseIndex + 1,
            PhaseContent<TSubject, TContext>(phase, PipelinePhaseRelation.After(reference))
        )
    }

    /**
     * Inserts [phase] as [PhaseContent] before the [reference] phase.
     *
     * @since 1.3.4
     */
    fun insertPhaseBefore(reference: PipelinePhase, phase: PipelinePhase) {
        if (hasPhase(phase)) return
        val index = findPhaseIndex(reference)
        if (index == -1) {
            throw IllegalArgumentException("Phase $reference not registered for this pipeline")
        }
        phasesRaw.add(
            index,
            PhaseContent<TSubject, TContext>(phase, PipelinePhaseRelation.Before(reference))
        )
    }

    /**
     * Adds [block] to the [phase] of this pipeline.
     *
     * @since 1.3.4
     */
    fun intercept(phase: PipelinePhase, block: PipelineInterceptor<TSubject, TContext>) {
        val phaseContent =
            findPhase(phase) ?: throw IllegalArgumentException("Phase $phase is not registered")
        phaseContent.addInterceptor(block)
        interceptorsQuantity++
        afterIntercepted()
    }

    /** @since 1.3.4 */
    open fun afterIntercepted() {
        nothing_to_do()
    }

    /** @since 1.3.4 */
    fun interceptorsForPhase(phase: PipelinePhase): List<PipelineInterceptor<TSubject, TContext>> {
        @Suppress("UNCHECKED_CAST")
        return phasesRaw.filterIsInstance<PhaseContent<*, *>>()
            .firstOrNull { it.phase == phase }
            ?.interceptors as List<PipelineInterceptor<TSubject, TContext>>
    }

    /** @since 1.3.4 */
    fun mergePhases(from: Pipeline<TSubject, TContext>) {
        val fromPhases = from.phasesRaw
        val toInsert = fromPhases.toMutableSet()
        while (toInsert.isNotEmpty()) {
            val iterator = toInsert.iterator()
            while (iterator.hasNext()) {
                val fromPhaseOrContent = iterator.next()

                val fromPhase = (fromPhaseOrContent as? PipelinePhase)
                    ?: (fromPhaseOrContent as PhaseContent<*, *>).phase

                if (hasPhase(fromPhase)) {
                    iterator.remove()
                } else {
                    val inserted = insertRelativePhase(fromPhaseOrContent, fromPhase)
                    if (inserted) {
                        iterator.remove()
                    }
                }
            }
        }
    }

    /** @since 1.3.4 */
    private fun mergeInterceptors(from: Pipeline<TSubject, TContext>) {
        val fromPhases = from.phasesRaw
        fromPhases.forEach { fromPhaseOrContent ->
            val fromPhase = (fromPhaseOrContent as? PipelinePhase)
                ?: (fromPhaseOrContent as PhaseContent<*, *>).phase

            if (fromPhaseOrContent is PhaseContent<*, *> && !fromPhaseOrContent.isEmpty) {
                @Suppress("UNCHECKED_CAST")
                fromPhaseOrContent as PhaseContent<TSubject, TContext>

                fromPhaseOrContent.addTo(findPhase(fromPhase)!!)
                interceptorsQuantity += fromPhaseOrContent.size
            }
        }
    }

    /** @since 1.3.4 */
    fun merge(from: Pipeline<TSubject, TContext>) {
        mergePhases(from)
        mergeInterceptors(from)
    }

    /** @since 1.3.4 */
    private fun createContext(
        context: TContext,
        subject: TSubject,
    ): PipelineContext<TSubject, TContext> {
        return SimplePipelineContext(context, subject, cacheInterceptors())
    }

    /**
     * Find phase matching [phase] in [phasesRaw]. If [PhaseContent] exists and
     * its phase matches [phase], return this object. If [PipelinePhase] and
     * [phase] match, convert [PipelinePhase] to [PhaseContent] and return.
     * Otherwise return empty.
     *
     * @see PhaseContent
     * @see PipelinePhase
     * @since 1.3.4
     */
    private fun findPhase(phase: PipelinePhase): PhaseContent<TSubject, TContext>? {
        val phasesList = phasesRaw
        for (index in phasesList.indices) {
            val current = phasesList[index]
            if (phase === current) {
                val content =
                    PhaseContent<TSubject, TContext>(
                        phase,
                        PipelinePhaseRelation.Last,
                        mutableListOf()
                    )
                phasesList[index] = content
                return content
            }

            if (current is PhaseContent<*, *> && current.phase == phase) {
                @Suppress("UNCHECKED_CAST")
                return current as PhaseContent<TSubject, TContext>
            }
        }
        return null
    }

    /**
     * Find index matching [phase] in [phasesRaw]. Returns the index if
     * [PhaseContent] exists and its phase matches [phase] .If there is a
     * matching [PipelinePhase], return the index of the [PipelinePhase].
     * Otherwise return -1.
     *
     * @return 1.3.4
     */
    private fun findPhaseIndex(phase: PipelinePhase): Int {
        val phasesList = phasesRaw
        for (index in phasesList.indices) {
            val raw = phasesList[index]
            if (phase === raw) return index
            if (raw is PhaseContent<*, *> && raw.phase === phase) return index
        }
        return -1
    }

    /**
     * Find phase matching [phase] in [phasesRaw]. Returns `true` if
     * [PhaseContent] exists and its phase matches [phase] , Returns `true`
     * if there is a matching [PipelinePhase]. Otherwise return `false` .
     *
     * @see PipelinePhase
     * @see PhaseContent
     * @since 1.3.4
     */
    private fun hasPhase(phase: PipelinePhase): Boolean {
        val phasesList = phasesRaw
        for (raw in phasesList) {
            if (phase === raw) return true
            if (raw is PhaseContent<*, *> && raw.phase === phase) return true
        }
        return false
    }

    /** @since 1.3.4 */
    private fun cacheInterceptors(): List<PipelineInterceptor<TSubject, TContext>> {
        if (interceptorsQuantity == 0) return emptyList()
        val phases = phasesRaw
        val result: MutableList<PipelineInterceptor<TSubject, TContext>> = mutableListOf()
        for (raw in phases) {
            @Suppress("UNCHECKED_CAST")
            val phase = (raw as? PhaseContent<TSubject, TContext>) ?: continue
            phase.addTo(result)
        }
        return result
    }

    /** @since 1.3.4 */
    private fun insertRelativePhase(fromPhaseOrContent: Any, fromPhase: PipelinePhase): Boolean {
        val fromPhaseRelation = when {
            fromPhaseOrContent === fromPhase -> PipelinePhaseRelation.Last
            else -> (fromPhaseOrContent as PhaseContent<*, *>).relation
        }

        when {
            fromPhaseRelation is PipelinePhaseRelation.Last ->
                addPhase(fromPhase)

            fromPhaseRelation is PipelinePhaseRelation.Before && hasPhase(fromPhaseRelation.relativeTo) ->
                insertPhaseBefore(fromPhaseRelation.relativeTo, fromPhase)

            fromPhaseRelation is PipelinePhaseRelation.After ->
                insertPhaseAfter(fromPhaseRelation.relativeTo, fromPhase)

            else -> return false
        }
        return true
    }
}
