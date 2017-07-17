# Overview
Watsonで作ったチャットボットをSlack Botとして実行する例

It is licensed under [MIT](https://opensource.org/licenses/MIT).

# 実行例

Watsonで作ったチャットボットをSlackから実行します

<img src="https://riversun.github.io/wcs/img/watson_slacklet.gif" width=75%>

### Watson Workspace File(JSON)

この例で使用したWatson Conversationのworksapceファイルは以下からダウンロード可

https://riversun.github.io/wcs/org.riversun.WcsContextTestJa.zip

# ソースコード

```java

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
```

# maven

```xml
		<dependency>
			<groupId>org.riversun</groupId>
			<artifactId>wcs</artifactId>
			<version>1.0.2</version>
		</dependency>
		<dependency>
			<groupId>org.riversun</groupId>
			<artifactId>slacklet</artifactId>
			<version>1.0.2</version>
		</dependency>
```
