package com.github.shiraji.ccch.menu

import com.github.shiraji.ccch.domain.CommonCustomChannel
import com.intellij.ide.plugins.PluginHostsConfigurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.updateSettings.impl.UpdateSettings
import com.intellij.ui.components.labels.SwingActionLink
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JComponent

class CcchMenu : SearchableConfigurable {
    private var eapCheckbox: JCheckBox? = null
    private var alphaCheckbox: JCheckBox? = null
    private var betaCheckbox: JCheckBox? = null
    private var nightlyCheckbox: JCheckBox? = null
    private var root: JPanel? = null
    private var actionLink: SwingActionLink? = null
    private var waringLabel: JLabel? = null

    override fun isModified(): Boolean {
        actionLink?.let { it.isEnabled = false }
        val list = UpdateSettings.getInstance().storedPluginHosts
        eapCheckbox?.let { if (isModifiedCheckbox(list, it, CommonCustomChannel.EAP.url)) return true }
        alphaCheckbox?.let { if (isModifiedCheckbox(list, it, CommonCustomChannel.ALPHA.url)) return true }
        betaCheckbox?.let { if (isModifiedCheckbox(list, it, CommonCustomChannel.BETA.url)) return true }
        nightlyCheckbox?.let { if (isModifiedCheckbox(list, it, CommonCustomChannel.NIGHTLY.url)) return true }
        actionLink?.let { it.isEnabled = true }
        return false
    }

    override fun getId(): String {
        return "com.github.shiraji.ccch.menu.CcchMenu"
    }

    private fun isModifiedCheckbox(list: List<String>, checkbox: JCheckBox, url: String): Boolean {
        val isContain = list.contains(url)
        return (checkbox.isSelected && !isContain) || (!checkbox.isSelected && isContain)
    }

    override fun getDisplayName(): String {
        return "Common custom release channel"
    }

    override fun apply() {
        val list = UpdateSettings.getInstance().storedPluginHosts
        val set = list.toMutableSet()
        list.clear()
        eapCheckbox?.let { applyChannel(set, it, CommonCustomChannel.EAP.url) }
        alphaCheckbox?.let { applyChannel(set, it, CommonCustomChannel.ALPHA.url) }
        betaCheckbox?.let { applyChannel(set, it, CommonCustomChannel.BETA.url) }
        nightlyCheckbox?.let { applyChannel(set, it, CommonCustomChannel.NIGHTLY.url) }
        list.addAll(set)
        waringLabel?.isVisible = true
    }

    private fun applyChannel(urls: MutableSet<String>, checkbox: JCheckBox, url: String) {
        if (checkbox.isSelected) {
            urls.add(url)
        } else {
            urls.remove(url)
        }
    }

    fun createUIComponents() {
        actionLink = SwingActionLink(object : AbstractAction("Open custom release channel dialog") {
            override fun actionPerformed(e: ActionEvent?) {
                if (ShowSettingsUtil.getInstance().editConfigurable(root, PluginHostsConfigurable())) {
                    syncUI()
                }
            }
        })
    }

    override fun createComponent(): JComponent? {
        syncUI()
        return root
    }

    private fun syncUI() {
        val list = UpdateSettings.getInstance().storedPluginHosts
        eapCheckbox?.let { it.isSelected = list.contains(CommonCustomChannel.EAP.url) }
        alphaCheckbox?.let { it.isSelected = list.contains(CommonCustomChannel.ALPHA.url) }
        betaCheckbox?.let { it.isSelected = list.contains(CommonCustomChannel.BETA.url) }
        nightlyCheckbox?.let { it.isSelected = list.contains(CommonCustomChannel.NIGHTLY.url) }
        actionLink?.let { it.isEnabled = true }
    }

    override fun reset() {
        super.reset()
        syncUI()
    }
}