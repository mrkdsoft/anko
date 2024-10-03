/*
 * Copyright 2016 JetBrains s.r.o.
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

package org.jetbrains.kotlin.android.dslpreview

import com.intellij.openapi.util.ModificationTracker
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.impl.PsiTreeChangeEventImpl
import com.intellij.psi.impl.PsiTreeChangePreprocessor
import org.jetbrains.kotlin.psi.KtFile
import java.util.concurrent.atomic.AtomicLong

class SourceFileModificationTracker : PsiTreeChangePreprocessor, ModificationTracker {

    private val counter = AtomicLong()

    companion object {
        private val HANDLED_EVENTS = setOf(
                PsiTreeChangeEventImpl.PsiEventType.CHILD_ADDED,
                PsiTreeChangeEventImpl.PsiEventType.CHILD_MOVED,
                PsiTreeChangeEventImpl.PsiEventType.CHILD_REMOVED,
                PsiTreeChangeEventImpl.PsiEventType.CHILD_REPLACED,
                PsiTreeChangeEventImpl.PsiEventType.CHILDREN_CHANGED)
    }

    override fun treeChanged(event: PsiTreeChangeEventImpl) {
        if (event.code in HANDLED_EVENTS) {
            val file = event.file
            if (file is KtFile || file is PsiJavaFile) counter.incrementAndGet()
        }
    }

    override fun getModificationCount() = counter.get()
}