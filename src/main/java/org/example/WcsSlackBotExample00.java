/*
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in the 
 * Software without restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package org.example;

import java.io.IOException;

import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;
import org.riversun.wcs.WcsClient;
import org.riversun.xternal.simpleslackapi.SlackUser;

/**
 * 
 * Watson Conversationを活用したSlackBotサンプル<br>
 * <p>
 * このSlackBotにダイレクトメッセージを送ると、Watson Conversation Workspaceの対話フローを実行
 *
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class WcsSlackBotExample00 {

    // ここを編集してWatsonの認証情報を入力
    private static final String WATSON_CONVERSATION_USERNAME = "EDIT_ME_USERNAME_HERE";
    private static final String WATSON_CONVERSATION_PASSWORD = "EDIT_ME_PASSWORD_HERE";
    private static final String WATSON_CONVERSATION_WORKSPACE_ID = "EDIT_ME_WORKSPACE_ID_HERE";

    // ここを編集してSlack bot API tokenを入力
    private static final String SLACK_BOT_API_TOKEN = "EDIT_ME_SLACK_API_TOKEN";

    public static void main(String[] args) throws IOException {

        final WcsClient watson = new WcsClient(
                WATSON_CONVERSATION_USERNAME,
                WATSON_CONVERSATION_PASSWORD,
                WATSON_CONVERSATION_WORKSPACE_ID);

        SlackletService slackService = new SlackletService(SLACK_BOT_API_TOKEN);

        // slackletを追加する
        slackService.addSlacklet(new Slacklet() {

            @Override
            public void onDirectMessagePosted(SlackletRequest req, SlackletResponse resp) {

                // BOT宛のダイレクトメッセージがポストされた

                // メッセージを送信してきたslackユーザー
                SlackUser slackUser = req.getSender();

                // メッセージの内容（テキスト）
                String userInputText = req.getContent();

                // slackユーザーのidを取得する、そして、そのslackユーザーのidをWatsonの一意ユーザーidとする
                String wcsClientId = slackUser.getId();

                // Watsonにユーザーが入力したテキストを送り、Watsonの出力(outputText)を受け取る
                String botOutputText = watson.sendMessageForText(wcsClientId, userInputText);

                // slackにWatsonからのレスポンスを表示する
                slackService.sendDirectMessageTo(slackUser, botOutputText);

            }

        });

        // slackletserviceを開始(slackに接続)
        slackService.start();

    }

}
