package com.github.yinssh1005.idepluginusingtemplate.toolWindow

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefBrowserBase
import com.intellij.ui.jcef.JBCefJSQuery


class MyToolWindowFactorySec : ToolWindowFactory {
    override fun createToolWindowContent(pj: Project, tw: ToolWindow) {

        val myToolWindow = MyToolWindowFactorySec.MyToolWindowSec(tw)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        tw.contentManager.addContent(content)
    }

    class MyToolWindowSec(tw: ToolWindow) {
        fun getContent() = JBPanel<JBPanel<*>>().apply {
//            val label = JBLabel(MyBundle.message("randomLabel", "?"))
//
//            add(label)
//            add(JButton(MyBundle.message("shuffle")).apply {
//                addActionListener {
//                    label.text = MyBundle.message("randomLabel", service.getRandomNumber())
//                }
//            })

            val browser = JBCefBrowser()

            val openLinkQuery = JBCefJSQuery.create(browser as JBCefBrowserBase) // 1
//            openLinkQuery.addHandler({ link: String? ->  // 2
//                if (LinkUtil.isExternal(link)) {
//                    BrowserUtil.browse((link)!!)
//                } else {
//                    EditorUtil.openFileInEditor(link)
//                }
//                null // 3
//            })
            BrowserUtil.browse(("http://localhost:8099"))
            browser.cefBrowser.executeJavaScript( // 4
                "window.openLink = function(link) {" +
                        openLinkQuery.inject("link") +  // 5
                        "};",
                browser.cefBrowser.url, 0
            )

            browser.cefBrowser.executeJavaScript( // 6
                """
                document.addEventListener('click', function (e) {
                  const link = e.target.closest('a').href;
                  if (link) {
                    window.openLink(link);
                  }
                });
                """.trimIndent(),
                browser.cefBrowser.url, 0
            )

            this.add(browser.component)
        }
    }

}

