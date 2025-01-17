package org.kamiblue.client.gui.rgui.component

import org.kamiblue.client.module.modules.client.ClickGUI
import org.kamiblue.client.setting.settings.impl.primitive.StringSetting
import org.kamiblue.client.util.math.Vec2f
import org.lwjgl.input.Keyboard
import kotlin.math.max

class StringButton(val setting: StringSetting) : BooleanSlider(setting.name, 1.0, setting.description, setting.visibility) {

    override val isBold
        get() = setting.isModified && ClickGUI.showModifiedInBold

    override fun onDisplayed() {
        super.onDisplayed()
        value = 1.0
    }

    override fun onStopListening(success: Boolean) {
        if (success) {
            setting.setValue(componentName)
        }

        super.onStopListening(success)
        componentName = name
        value = 1.0
    }

    override fun onMouseInput(mousePos: Vec2f) {
        super.onMouseInput(mousePos)
        if (!listening) {
            componentName = if (mouseState == MouseState.NONE) name
            else setting.value
        }
    }

    override fun onTick() {
        super.onTick()
        if (!listening) {
            componentName = if (mouseState != MouseState.NONE) setting.value
            else name
        }
    }

    override fun onRelease(mousePos: Vec2f, buttonId: Int) {
        super.onRelease(mousePos, buttonId)
        if (buttonId == 0 && !listening) {
            listening = true
            componentName = setting.value
            value = 0.0
        } else {
            onStopListening(true)
        }
    }

    override fun onKeyInput(keyCode: Int, keyState: Boolean) {
        super.onKeyInput(keyCode, keyState)
        val typedChar = Keyboard.getEventCharacter()
        if (keyState) {
            when (keyCode) {
                Keyboard.KEY_RETURN -> {
                    onStopListening(true)
                }
                Keyboard.KEY_BACK, Keyboard.KEY_DELETE -> {
                    componentName = componentName.substring(0, max(componentName.length - 1, 0))
                }
                else -> if (typedChar >= ' ') {
                    componentName += typedChar
                }
            }
        }
    }
}