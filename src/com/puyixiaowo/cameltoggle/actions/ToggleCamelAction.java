package com.puyixiaowo.cameltoggle.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import com.puyixiaowo.cameltoggle.utils.CamelCaseUtils;

/**
 * @author Moses
 * @date 2017-07-28 14:07
 */
public class ToggleCamelAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //Get all the required data from data keys
        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();

        if (!selectionModel.hasSelection()) {
            selectionModel.selectWordAtCaret(true);
        }

        int start = selectionModel.getSelectionStart();
        int end = selectionModel.getSelectionEnd();

        String text = document.getText(new TextRange(start, end));

        String replacement = CamelCaseUtils.checkIsCamelCase(text) ? CamelCaseUtils.toUnderlineName(text) :
                CamelCaseUtils.toCamelCase(text);
        //New instance of Runnable to make a replacement
        Runnable runnable = () -> document.replaceString(start, end, replacement);
        //Making the replacement
        WriteCommandAction.runWriteCommandAction(project, runnable);
        selectionModel.removeSelection();
    }
}
