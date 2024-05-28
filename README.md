# Chat API Assignment

## DB 立ち上がり方
/docker ディレクトリーにあるdocker-composeでMySQLデータベースを作成する

### コンテーナー作成コマンド
```shell
    docker-compose up --build -d

```

### コンテーナー削除コマンド
```shell
    docker-compose down

```
 - DBのUserMstテーブルに5人ユーザーが作成されるので、そのユーザーを使用する。
 - DB作成sqlファイルは/docker/schemas/chat.sqlにあります

# Unit Test
Junit 5で作成<br>
ServiceとControllerの全てのClassのテスト済み

# API 実行エビデンス

## 1対1チャット

### チャット作成
ユーザー1とユーザー2でチャット作成 <br>
isGroup: グループチャットではないのでFalse <br>
結果: チャットID:4 作成される
![Create1on1Chat.png](1on1ChatImgs%2FCreate1on1Chat.png)

### メッセージ投稿
ユーザー1が2件メッセージを投稿する (chatID:4にて)
結果: メッセージID 19と20作成される
![CreateMessage1.png](1on1ChatImgs%2FCreateMessage1.png)

![CreateMessage2.png](1on1ChatImgs%2FCreateMessage2.png)

### メッセージを取得
ユーザー1(投稿者)としてメッセージの一覧を取得したら、readCountとreadStatusが変わらない
![GetMessagesAsMessageCreator.png](1on1ChatImgs%2FGetMessagesAsMessageCreator.png)

ユーザー2がメッセージの一覧を取得したら、<br>
   - readCount + 1 <br>
   - 2人チャットなので、既読に変わる
   
![GetMessagesAsUser2.png](1on1ChatImgs%2FGetMessagesAsUser2.png)

## グループチャット

### チャットお作成
ユーザー1、ユーザー2、ユーザ3でチャット作成 <br>
isGroup: グループチャットなのでTrue <br>
結果: チャットID:5 作成される
![CreateGroupChat.png](GroupChatImgs%2FCreateGroupChat.png)

### メッセージ投稿
ユーザー1が2件メッセージを投稿する (chatID:4にて)
結果: メッセージID 21と22作成される
![CreateMessagesInGroupChat.png](GroupChatImgs%2FCreateMessagesInGroupChat.png)

### メッセージ取得
ユーザー1(投稿者)としてメッセージの一覧を取得したら、readCountとreadStatusが変わらない
![GetMessagesAsMessageCreator.png](GroupChatImgs%2FGetMessagesAsMessageCreator.png)

ユーザー2がメッセージの一覧を取得したら、<br>
- readCount = 1<br>
- 3人のグループチャットなので、未読のまま
![GetMessagesAsUser2.png](GroupChatImgs%2FGetMessagesAsUser2.png)

ユーザー3がメッセージの一覧を取得したら、<br>
- readCount = 2<br>
- 3人のグループチャットなので、既読に変わる
![GetMessagesAsUser3.png](GroupChatImgs%2FGetMessagesAsUser3.png)

## メッセージ削除
1対1チャットで作成されたメッセージID:19を削除する
結果：successがTrueなので削除成功
![DeleteMessage.png](1on1ChatImgs%2FDeleteMessage.png)

削除確認 <br>
メッセージID:19削除された
![CheckDeletion.png](1on1ChatImgs%2FCheckDeletion.png)

### 通知設定
notificationPushがtrueなのでPush通知のみ
![PushSettings1.png](1on1ChatImgs%2FPushSettings1.png)

notificationPushとnotificationEmailがTrueの場合<br>
両方通知設定されます
![PushSettings2.png](1on1ChatImgs%2FPushSettings2.png)