AI Writing Assistant
===================

Spring Boot REST API + DeepSeek AI による英文ライティングアシスタント。

概要
----
ユーザーが入力した英文を、DeepSeek API を使って3つのスタイル
（Formal / Natural / Business）に自動改善するWebアプリケーション。

技術スタック
----------
- Java 17
- Spring Boot 3.5.0
- Spring Data JPA + Hibernate Validator
- H2 In-Memory Database
- OkHttp 4.12.0
- SLF4J (structured logging)
- DeepSeek Chat API

プロジェクト構成
--------------
ai-writing-assistant/
  pom.xml
  src/main/java/com/example/aiwriting/
    AiWritingAssistantApplication.java          -- エントリポイント
    model/
      ImprovementRecord.java                     -- JPAエンティティ
    repository/
      ImprovementRecordRepository.java           -- JPAリポジトリ
    dto/
      ImproveRequest.java                        -- リクエストDTO
      ImproveResponse.java                       -- レスポンスDTO（4キー構造）
    service/
      DeepSeekService.java                       -- DeepSeek API呼び出し
      ImprovementService.java                    -- ビジネスロジック
    controller/
      ImprovementController.java                 -- RESTコントローラ（ログ付き）
      GlobalExceptionHandler.java                -- グローバル例外ハンドラ
  src/main/resources/
    application.properties                       -- 設定ファイル
    static/
      index.html                                 -- フロントエンドUI

API エンドポイント
------------------
POST /api/improve      -- テキスト改善リクエスト（@Valid バリデーション付き）
  Request:  {"text": "..."}   (5〜2000文字必須)
  Response: {"id":1, "original":"...", "formal":"...", "natural":"...", "business":"...", "createdAt":"..."}
  Error 400: {"error": "text: Text must be between 5 and 2000 characters"}

GET  /api/history       -- 過去の改善履歴を取得

ブラウザUI
---------
http://localhost:8080 を開くと、
- 入力ボックス（文字数カウンター付き）
- Formal / Natural / Business の3枚のカード（カラーアクセントバー付き）
- タイプライターアニメーション + 3色のドット思考アニメーション
- 各カードにCopyボタン（"✓ Copied!"フィードバック付き）
- 下部にHistoryセクション（リアルタイム検索 + キーワードハイライト）
が表示される。

セットアップ手順
--------------
1. JDK 17以上をインストール
   winget install EclipseAdoptium.Temurin.17.JDK

2. DeepSeek APIキーを取得
   https://platform.deepseek.com/

3. プロジェクトをクローンして起動
   git clone https://github.com/liu-jie-ux/ai-writing-assistant.git
   cd ai-writing-assistant
   set DEEPSEEK_API_KEY=sk-your-key-here
   mvnw.cmd spring-boot:run

4. ブラウザで開く
   http://localhost:8080

動作確認（curl）
--------------
curl -X POST http://localhost:8080/api/improve ^
  -H "Content-Type: application/json" ^
  -d "{\"text\": \"I think this product is kind of good but maybe could be better.\"}"

curl http://localhost:8080/api/history

バージョン履歴
------------
V1 - 単一スタイル改善（improvedText）
V2 - マルチスタイル改善（formal / natural / business）
V3 - ダークテーマフロントエンド
V4 - ライトテーマリデザイン
V5 - バリデーション + 例外ハンドラ + 構造化ログ + Copyボタン + History検索（現在）

ライセンス
---------
MIT License
