# Kotlin API ガイドライン準拠状況レポート

対象: [iolite](https://github.com/ysknsid25/iolite)
基準: [Kotlin Library creator's guidelines](https://kotlinlang.org/docs/api-guidelines-introduction.html)
評価日: 2026-05-06
評価対象バージョン: `beta-v3`

---

## 総評

**準拠度の概観: おおむね 50〜60%。**

Value Object ライブラリとしての方向性はガイドラインの精神に合致している（`@JvmInline value class`、`require()` による境界バリデーション、不変性、副作用のないシンプルな API）。一方で、**デバッグ容易性と後方互換性のインフラがほぼ未整備**で、これがメインの弱点。バージョンが `beta-v3` のうちに対処すべき項目が多い。

---

## 1. Readability（読みやすさ）— 部分的に準拠

| ガイドライン | 状態 | 該当箇所 |
|---|---|---|
| Boolean 引数を避ける | 違反 | `datetime/DateTime.kt:7-12` の `offset: Boolean = false, local: Boolean = false`。`Mode.OFFSET` / `Mode.LOCAL` のような enum か、`DateTime.local(...)` / `DateTime.offset(...)` といったファクトリ関数が望ましい。 |
| 数値型の適切な選択 | 準拠 | `Age` で `Int`、`UInt` を不要に使っていない。 |
| DSL / 拡張関数の活用 | 部分的 | `StringValueObject` のメソッドチェーン（`notEmpty().min(10).max(20)…`）は合成的でガイドライン精神に合う。一方、メンバ関数のみで構成されており、ガイドラインが推奨する「拡張関数として補助機能を切り出す」パターンは未採用。 |
| 関数シグネチャを膨張させない | 準拠 | パラメータ数は最小限。 |

## 2. Predictability（予測可能性）— おおむね準拠

| ガイドライン | 状態 | 該当箇所 |
|---|---|---|
| `require()` での入力検証 | 準拠 | 全 Value Object で一貫して使用。エラーメッセージも具体的。 |
| 妥当なデフォルト | 準拠 | `DateTime` のデフォルト値、`safeParse` のデフォルト実装（`ValueObject.kt:5-11`）など。 |
| 拡張余地を残す | 準拠 | `ValueObject<T>` インターフェースで誰でも独自 VO を実装可能。 |
| 不正な拡張を防ぐ（sealed） | 違反 | `ValueObject` は `sealed interface` になっておらず、ライブラリ外の任意の型が `ValueObject` を実装できる。利用者の独自 VO を許容する設計意図ならこれで OK だが、その方針は明文化されていない。 |
| エラーメッセージにセンシティブ情報を含めない | 違反（要修正） | `personal/CreditCardNumber.kt:11,14,17` がカード番号を、`personal/Email.kt:16` がメールアドレスを、平文でメッセージに埋め込んでいる。ガイドラインが明示的に避けるよう求めている代表例。 |
| 可変状態を露出しない | 準拠 | 全て `value class`／`val`。 |

## 3. Debuggability（デバッグ容易性）— 大きな弱点

| ガイドライン | 状態 |
|---|---|
| 意味のある `toString()` | 違反 | 全クラスで未実装。`@JvmInline value class` の自動生成 `toString()` は `Email(value=...)` 形式で、ガイドラインが要求する「内容を意図的に表現する」レベルではない。さらに、自動 `toString()` は機微情報をそのまま出力するため、`CreditCardNumber` で深刻な漏洩リスク。 |
| 例外ハンドリングポリシーの文書化 | 違反 | ライブラリ固有の例外型（例: `ValidationException`）が存在せず、`IllegalArgumentException` を直接スロー。`safeParse` も `IllegalArgumentException` のみキャッチしているため、利用者コードのバグ由来の `IllegalArgumentException` まで成功側でない `Result.failure` に取り込んでしまう。 |
| 例外の `cause` 連鎖 | 違反 | 該当ロジックなし。 |

この領域が最も改善効果が大きい。

## 4. Backward Compatibility（後方互換性）— インフラ未整備

| ガイドライン | 状態 |
|---|---|
| Binary Compatibility Validator | 違反 | Gradle プラグイン未導入、`.api` ファイルなし。 |
| Explicit API mode | 違反 | `kotlin { explicitApi() }` 未設定。 |
| 戻り値型の明示 | 準拠 | おおむね明示されている（インターフェースのオーバーライドのため）。 |
| `data class` を public API で使わない | 準拠 | 使用ゼロ。 |
| デフォルト引数による拡張を避ける | 注意 | `DateTime` がデフォルト引数を 3 つ持つ。ここに引数を追加するとバイナリ非互換になる。手動オーバーロードへの移行推奨。 |
| `@RequiresOptIn` / `@Deprecated` 運用 | 違反 | 未使用（`beta` 段階なので必須ではないが、1.0 前にポリシー策定が必要）。 |
| `@JvmInline value class` | 準拠 | JVM 互換性として妥当な選択。 |

## 5. Multiplatform Support — 看板倒れ気味

`build.gradle.kts:11` で `kotlin("multiplatform")` を宣言しているものの、実際のターゲットは `jvm()` のみ（`build.gradle.kts:21-25`）。`@JvmInline` を全 VO で使っているため、現状 JS/Native ターゲットを追加しても実質 JVM 専用。klibs.io 上の表示目的だとしても、README の "Inspired by Zod" として他プラットフォーム展開を期待する利用者には誤解を招く。

## 6. Documentation

- README は使い方が網羅されていて良好。
- 一方、ソース側の **KDoc がほぼゼロ**。Dokka でサイトを生成しているのに、各 VO に「何を、どんなルールで、どんな正規化を行うか」のドキュメントがない（例: `Email` が `lowercase + trim` で正規化することはコードを読まないと分からない）。

---

## 優先度順の改善提案（サマリ）

1. 機微情報をエラーメッセージから除去（`CreditCardNumber`、`Email`）— セキュリティ影響が直接的。
2. `toString()` を全 Value Object で明示実装し、`CreditCardNumber` ではマスクを行う。
3. ライブラリ固有の例外型 `ValidationException(message, cause)` を導入し、`safeParse` でこれをキャッチするよう統一。
4. Binary Compatibility Validator + Explicit API mode を導入（`beta` を抜ける前が好機）。
5. `DateTime` の Boolean 引数を enum/ファクトリへ置換。
6. KDoc を各 VO の `parse()` に追加（受理仕様、正規化、例外）。
7. Multiplatform を本当に行うなら `@JvmInline` を外して JS/Native ターゲットを追加、行わないなら KMP を畳んで純 JVM 構成にする。

---

# Issue 化用詳細

以下、各改善項目を Issue としてそのまま起票できるよう、**背景／方針／詳細** の 3 セクション構成で記述する。Priority は上から順に高い。

---

## Issue #1: 機微情報をエラーメッセージから除去する

### 背景
Kotlin API ガイドラインの [Predictability — Validate inputs and state](https://kotlinlang.org/docs/api-guidelines-predictability.html#validate-inputs-and-state) は、`require()`／`check()` のメッセージに **パスワードやクレジットカード番号などのセンシティブ情報を含めてはならない** と明示している。エラーメッセージは例外オブジェクトに格納され、スタックトレースとしてログ・APM・エラー集計サービス（Sentry 等）にそのまま流れる。利用者の運用環境でログが第三者に閲覧されると、バリデーション失敗時に**入力値そのものが漏洩**する。

現在の実装は以下のように、検証対象の値をそのまま `$value` で埋め込んでいる:

- `src/commonMain/kotlin/personal/CreditCardNumber.kt:11,14,17` — カード番号
- `src/commonMain/kotlin/personal/Email.kt:16` — メールアドレス
- `src/commonMain/kotlin/personal/JpPhoneNumber.kt`、`personal/JpPostalCode.kt` も同様の傾向（要確認）

`Email` は GDPR・個人情報保護法上 PII（個人識別情報）。`CreditCardNumber` は PCI DSS の保護対象。どちらも本番ログに残してはいけない情報。

### 方針
**機微情報を扱う Value Object については、エラーメッセージから入力値そのものを除去する。** 代わりに「どの検証が失敗したか」を示す識別子のみを返す。デバッグ用途で値を見たい開発者向けには、開発時のみ有効な詳細メッセージを別途用意するのではなく、**ローカルでのテスト時に値を直接渡して再現する** 運用を前提とする（ガイドラインのスタンスに合わせる）。

### 詳細

**対象ファイル:**
- `src/commonMain/kotlin/personal/CreditCardNumber.kt`
- `src/commonMain/kotlin/personal/Email.kt`
- `src/commonMain/kotlin/personal/JpPhoneNumber.kt`
- `src/commonMain/kotlin/personal/JpPostalCode.kt`

**変更例（CreditCardNumber）:**

```kotlin
// Before
require(CREDIT_CARD_REGEX.matches(value)) {
    "Invalid credit card format: $value"
}

// After
require(CREDIT_CARD_REGEX.matches(value)) {
    "Invalid credit card format"
}
```

**Email についても同様:**

```kotlin
// Before
"Invalid email address: $value"
// After
"Invalid email address"
```

**判断基準:**
- `personal/` パッケージ配下は原則すべて値を出力しない。
- `network/`（IPv4、URL 等）や `id/`（UUID）、`encoding/`（Base64）、`strings/`、`datetime/` は値が機微でないため現状維持で良い（むしろデバッグ性を取る）。

**テスト:**
- 既存の例外メッセージを assertion している箇所がないか `commonTest` 全体を grep して確認。
- 必要なら「メッセージに値が含まれないこと」を保証するテストを `personal/` 配下のテストに追加。

**関連:** Issue #2（`toString()` マスク）と同じ問題系列なので、可能ならまとめて 1 PR で対応するか、相互参照する。

---

## Issue #2: 全 Value Object に明示的な `toString()` を実装する（機微情報はマスク）

### 背景
Kotlin API ガイドラインの [Debuggability — Provide a toString() method for stateful types](https://kotlinlang.org/docs/api-guidelines-debuggability.html) は、状態を持つすべての型に意味のある `toString()` を実装することを求めている。

現状、すべての VO は `@JvmInline value class` で `toString()` を未実装。Kotlin コンパイラが自動生成する `toString()` は `ClassName(value=...)` 形式となり、以下の問題がある:

1. **機微情報の漏洩**: `CreditCardNumber("4111-1111-1111-1111").toString()` は `CreditCardNumber(value=4111-1111-1111-1111)` をそのまま出力する。ログ／APM／例外スタックトレース／IDE のデバッガ表示などあらゆる場所で漏れる。
2. **API 契約として不安定**: 自動生成 `toString()` のフォーマットはコンパイラ実装依存。将来 Kotlin が `@JvmInline value class` の `toString()` 仕様を変更した場合、それに依存していたユーザーコードが壊れる。ガイドラインは「`toString()` フォーマットを API 契約として明示するか、しないか」を**意図的に選べ**と要求している。
3. **構造化情報が出ない**: `DateTime(value, precision, offset, local)` のような複合状態を持つ型でも、現状 `value` しか表示されない。

### 方針
- **全 VO に `override fun toString(): String` を明示実装する。**
- フォーマットは原則 `ClassName(value)`（簡潔・パース可能）を基本とする。
- **機微情報を扱う VO はマスクして出力する。** マスク方針は型ごとに決める。
- マスクのフォーマットは KDoc で「これは API 契約である／ないか」を明示する。

### 詳細

**対象ファイル:** `src/commonMain/kotlin/` 配下のすべての Value Object（約 17 ファイル）

**実装例:**

```kotlin
// Email — ローカル部の先頭1文字以外をマスク
@JvmInline
value class Email(private val value: String) : ValueObject<String> {
    override fun parse(): String { /* ... */ }

    /**
     * Returns a masked representation: e.g. `Email(j***@example.com)`.
     * The exact mask format is not part of the public contract.
     */
    override fun toString(): String {
        val at = value.indexOf('@')
        if (at <= 0) return "Email(***)"
        val local = value.substring(0, at)
        val domain = value.substring(at)
        val masked = local.first() + "***"
        return "Email($masked$domain)"
    }
}

// CreditCardNumber — 末尾4桁以外をマスク
override fun toString(): String {
    val digits = value.filter { it.isDigit() }
    val last4 = digits.takeLast(4).padStart(4, '*')
    return "CreditCardNumber(****-****-****-$last4)"
}

// JpPhoneNumber、JpPostalCode、Age も同様にマスクまたは伏せる方針を決める

// 機微でない VO（例: Url、Uuid、Cidr、Date、Base64）はそのまま値を出す
override fun toString(): String = "Url($value)"
```

**マスク方針の決定表:**

| VO | マスク方針 |
|---|---|
| `Email` | ローカル部の先頭1文字 + `***` + ドメイン |
| `CreditCardNumber` | 末尾 4 桁のみ表示 |
| `JpPhoneNumber` | 末尾 4 桁のみ表示 |
| `JpPostalCode` | フルマスク（地域特定可能なため） |
| `Age` | フルマスク（個人特定の補助情報になりうる）／要議論 |
| 上記以外 | マスクなし、値をそのまま出力 |

**`DateTime` のような複合状態型:**
```kotlin
override fun toString(): String =
    "DateTime(value=$value, precision=$precision, offset=$offset, local=$local)"
```

**注意点:**
- `@JvmInline value class` でも `toString()` は普通にオーバーライドできる。`equals`／`hashCode` は基底値由来でそのままで OK。
- KDoc で「フォーマットは API 契約**ではない**」ことを明示し、将来の変更余地を残す。
- パース可能なフォーマット（ガイドラインで推奨）にしたい場合は `companion object fun parseToString(s: String)` を提供する選択肢もあるが、現時点では over-engineering なのでスキップ。

**テスト:**
- 各 VO に `toString` のスナップショットテストを 1 ケース追加（マスク済み出力の確認）。
- Konsist ルール候補: 「`personal/` 配下の `value class` は `toString` をオーバーライドしている」を `ValueObjectRuleTest` に追加。

---

## Issue #3: ライブラリ固有の例外型 `ValidationException` を導入する

### 背景
Kotlin API ガイドラインの [Debuggability — Adopt and document an exception-handling policy](https://kotlinlang.org/docs/api-guidelines-debuggability.html) は次を要求する:

- ライブラリ固有の例外型を持ち、内部依存ライブラリの例外を**そのまま外に出さない**。
- 例外型は **エラーの種類を識別可能** にする。
- `cause` で原因例外をたどれるようにする。
- 例外ハンドリングポリシーを **明示的にドキュメント化する**。

現状 iolite は標準の `IllegalArgumentException` を投げている。これは以下の問題を生む:

1. **`safeParse` の取りこぼしリスク**: `ValueObject.kt:5-11` の `safeParse` は `IllegalArgumentException` のみキャッチする。`parse()` 実装中で **利用者コードのバグや別ライブラリ起因** の `IllegalArgumentException` が発生した場合（例: ユーザー定義の `customerValidation` ラムダ内で別の `require` が失敗した場合）、それも `Result.failure` に押し込まれてしまい、本来のバグが隠蔽される。
2. **エラー識別が困難**: 利用者側で「iolite のバリデーション失敗」と「自コードの不正引数エラー」を区別する手段がない。
3. **構造化情報の欠如**: どの VO が、どの値で、どんなルール違反で失敗したか、を例外オブジェクトから機械的に取り出せない（メッセージ文字列をパースするしかない）。

### 方針
- ルート例外型 `IoliteException`（または `ValidationException`）を導入し、すべてのバリデーション失敗をこれでスローする。
- `safeParse` は `IoliteException` のみキャッチする。
- 既存ユーザーの互換性のため、`IoliteException` は `IllegalArgumentException` を継承する（ガイドラインで許容されている `cause` 経路ではなく型階層側の互換）。
- 例外には少なくとも「VO 種別」「失敗ルール」を構造化フィールドとして持たせる。
- 例外ハンドリングポリシーを `README.md` または `docs/exceptions.md` に明文化する。

### 詳細

**新規ファイル:** `src/commonMain/kotlin/IoliteException.kt`

```kotlin
package iolite

/**
 * Base exception thrown by all iolite Value Objects when validation fails.
 *
 * Extends [IllegalArgumentException] so existing `try { ... } catch (e: IllegalArgumentException)`
 * code keeps working, but library users SHOULD prefer catching this type to
 * distinguish iolite validation failures from other argument errors.
 */
public open class IoliteException(
    public val target: String,
    public val rule: String,
    message: String,
    cause: Throwable? = null,
) : IllegalArgumentException(message, cause)
```

**`ValueObject.kt` の更新:**

```kotlin
interface ValueObject<T> {
    fun parse(): T
    fun safeParse(): Result<T> {
        return try {
            Result.success(parse())
        } catch (e: IoliteException) {
            Result.failure(e)
        }
    }
}
```

**各 VO の `require` 置き換え例（Email）:**

```kotlin
// Before
require(regex.matches(normalized)) { "Invalid email address: $value" }

// After
if (!regex.matches(normalized)) {
    throw IoliteException(
        target = "Email",
        rule = "format",
        message = "Invalid email address",
    )
}
```

`require` を直接置き換えるのは記述量が増えるため、ヘルパー関数を導入してもよい:

```kotlin
internal inline fun validate(target: String, rule: String, condition: () -> Boolean) {
    if (!condition()) {
        throw IoliteException(target = target, rule = rule, message = "$target validation failed: $rule")
    }
}
```

**`Result.failure` を `safeParse` で扱う際の利用者コード:**

```kotlin
val result = Email("bad").safeParse()
result.exceptionOrNull()?.let { e ->
    if (e is IoliteException) {
        log("validation failed: target=${e.target} rule=${e.rule}")
    }
}
```

**ドキュメント追加:**
- `README.md` に「Exception handling」セクションを追加。
- `parse()` がスローするのは `IoliteException` のみであること、`safeParse()` はそれだけをキャッチすることを明記。
- `IllegalArgumentException` を継承していること、その理由（後方互換）も明記。

**移行戦略:**
- `IoliteException` は `IllegalArgumentException` を継承するため、既存ユーザーコードは無変更で動く。
- ただし `safeParse` のキャッチ範囲が狭まる挙動変更があるため、CHANGELOG に記載し、できれば `1.0` リリースに合わせて入れる。

**テスト:**
- 各 VO の異常系テストを `assertFailsWith<IoliteException>` に置き換える。
- `IoliteException.target` と `rule` フィールドの内容を assert するテストを 1〜2 ケース追加。

---

## Issue #4: Binary Compatibility Validator と Explicit API mode を導入する

### 背景
Kotlin API ガイドラインの [Backward Compatibility](https://kotlinlang.org/docs/api-guidelines-backward-compatibility.html) は、ライブラリ作者がバイナリ互換性を**継続的に検証**する仕組みを持つことを強く推奨している。具体的なツールとして以下を挙げている:

- [Binary Compatibility Validator](https://github.com/Kotlin/binary-compatibility-validator)（Gradle plugin）— `.api` ダンプを生成し PR で差分レビュー
- Kotlin Gradle plugin 2.2.0+ 内蔵の binary compatibility validation
- `kotlin { explicitApi() }` — public API 宣言の戻り値型・可視性を強制

iolite は現在、これらをいずれも導入していない。バージョンが `beta-v3` で破壊的変更を入れやすいタイミングのうちに、**1.0 リリース後に「うっかりバイナリ非互換を入れて release してしまう」事故を防ぐ** 仕組みを整えるのが合理的。

また、Explicit API mode を入れていないことで、現状 `parse()` を含む public API 群の戻り値型が「インターフェース由来でたまたま明示されている」状態に依存している。`StringValueObject.notEmpty()` のような fluent API は戻り値型が明示されているが、コンパイラに強制されているわけではない。

### 方針
1. **Binary Compatibility Validator Gradle plugin を導入** し、`.api` ファイルを `api/` ディレクトリ配下にコミット。CI で `apiCheck` を実行。
2. **`kotlin { explicitApi() }` を有効化**。public 宣言に `public` 修飾子と戻り値型を強制。
3. これらを導入した上で、既存コードを修正してビルドが通る状態にする（`internal`／`private` を適切に追加、戻り値型の明示）。

### 詳細

**1. Binary Compatibility Validator の導入**

`build.gradle.kts` に追加:

```kotlin
plugins {
    // ...既存
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.3"
}

apiValidation {
    // 必要に応じて internal package を除外
    ignoredPackages.add("iolite.internal")
}
```

初回ダンプ:
```bash
./gradlew apiDump
```

`api/iolite.api`（または `iolite.klib.api`）が生成されるので git にコミット。CI（`.github/workflows/*.yml`）に以下を追加:

```yaml
- name: Check binary compatibility
  run: ./gradlew apiCheck
```

PR で API が変わるたびに `apiDump` を再実行 → 差分レビュー、というワークフローになる。

**2. Explicit API mode の有効化**

`build.gradle.kts` の `kotlin { ... }` ブロック内に追加:

```kotlin
kotlin {
    explicitApi()
    jvm { /* ... */ }
}
```

**修正が必要になる箇所（推定）:**

- `ValueObject.kt:3` の `interface ValueObject<T>` → `public interface ValueObject<T>`
- 各 VO の `value class Foo` → `public value class Foo`
- 各 VO の `companion object` 内のフィールドは `private` 指定済み → 影響なし
- `parse()` のオーバーライドは戻り値型明示済み → 影響なし
- `StringValueObject` の chain メソッド（`notEmpty()` 等）は戻り値型 `StringValueObject` 明示済み → 影響なし

実質、`public` 修飾子の追加が大半。

**3. 互換性チェックポリシーの整備**

`CONTRIBUTING.md` に追記:

- API を変更する PR では `./gradlew apiDump` を実行して `api/` 配下を更新すること
- レビュー時に `.api` の差分を確認すること
- バイナリ非互換になる変更（戻り値型の変更、引数の追加等）は major バージョン更新を必須とすること

**段階導入の推奨:**
- まず Binary Compatibility Validator のみ導入 → 現状のスナップショットを取る
- 次に Explicit API mode を有効化 → コンパイルが通るまで修正
- 最後に CI へ組み込む

**注意:**
- Kotlin Multiplatform プロジェクトでは `klib` 形式の `.api` ダンプ機能が validator のバージョンによって挙動が違う。最新の 0.16.x で KMP 対応済み。
- `beta-v3` のうちに API スナップショットを確定させ、`1.0.0` リリース時にこれを互換境界とすることが理想。

---

## Issue #5: `DateTime` の Boolean 引数を enum / ファクトリへ置換する

### 背景
Kotlin API ガイドラインの [Readability — Avoid using Boolean arguments in functions](https://kotlinlang.org/docs/api-guidelines-readability.html) は、関数引数として `Boolean` を使うことを避けるよう求めている。理由:

- `DateTime("2024-01-01T00:00:00", 3, true, false)` のような呼び出しは、引数の意味が読み取れない。
- IDE 補完で named argument が促されない言語（Java から呼ぶ場合等）では事故りやすい。
- 真偽の組み合わせが意味のない／矛盾する状態を許容してしまう（例: `offset=true, local=true`）。

該当箇所: `src/commonMain/kotlin/datetime/DateTime.kt:7-12`

```kotlin
class DateTime(
    private val value: String,
    private val precision: Int? = null,
    private val offset: Boolean = false,
    private val local: Boolean = false,
)
```

`offset` と `local` の組み合わせは「Z 固定 / Z または ±HH:MM オフセット / タイムゾーンなし（ローカル）」の **3 状態** を表す。本来 enum で表現すべきもの。

### 方針
- `offset: Boolean` と `local: Boolean` を 1 つの enum `DateTime.Zone` に統合する。
- ファクトリメソッド（`DateTime.utc(...)`, `DateTime.withOffset(...)`, `DateTime.local(...)`）も補助的に提供する。
- 後方互換のため旧 constructor は `@Deprecated(level = WARNING, replaceWith = ...)` で残し、次のメジャーバージョンで削除する。

### 詳細

**変更後の API（案）:**

```kotlin
class DateTime(
    private val value: String,
    private val precision: Int? = null,
    private val zone: Zone = Zone.UTC,
) : ValueObject<String> {

    public enum class Zone {
        /** Requires trailing 'Z'. */
        UTC,
        /** Accepts 'Z' or '±HH:MM' offset. */
        OFFSET,
        /** No timezone suffix (local time). */
        LOCAL,
    }

    override fun parse(): String { /* ... 既存ロジックの分岐を zone ベースに置換 */ }

    public companion object {
        public fun utc(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.UTC)

        public fun withOffset(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.OFFSET)

        public fun local(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.LOCAL)
    }
}
```

**呼び出し側の変化:**

```kotlin
// Before
DateTime("2024-01-01T00:00:00+09:00", precision = 0, offset = true)

// After (enum)
DateTime("2024-01-01T00:00:00+09:00", precision = 0, zone = DateTime.Zone.OFFSET)

// After (factory)
DateTime.withOffset("2024-01-01T00:00:00+09:00", precision = 0)
```

**deprecation:**

```kotlin
@Deprecated(
    message = "Use DateTime(value, precision, Zone) or factory methods.",
    replaceWith = ReplaceWith(
        "DateTime(value, precision, " +
            "if (local) DateTime.Zone.LOCAL " +
            "else if (offset) DateTime.Zone.OFFSET " +
            "else DateTime.Zone.UTC)"
    ),
    level = DeprecationLevel.WARNING,
)
constructor(
    value: String,
    precision: Int? = null,
    offset: Boolean = false,
    local: Boolean = false,
) : this(
    value = value,
    precision = precision,
    zone = when {
        local -> Zone.LOCAL
        offset -> Zone.OFFSET
        else -> Zone.UTC
    },
)
```

**追加検証:**
- 旧 API では `offset=true, local=true` のような無意味な組み合わせが許されていた。enum 化により**矛盾状態の発生自体がコンパイル時に不可能** になる。これは Predictability の改善でもある。

**テスト:**
- 既存の `DateTime` テストを enum ベースに書き換える。
- 旧 constructor が同じバリデーション結果を返すことを保証するパラメタライズドテストを 1 つ追加（deprecation を抜く前まで残す）。

**段階リリース:**
1. `beta-v4`: enum 追加、旧 constructor を `@Deprecated(WARNING)`。
2. `1.0.0`: WARNING のまま。
3. `1.1.0` または `2.0.0`: `@Deprecated(ERROR)` または削除。

---

## Issue #6: 全 Value Object の `parse()` に KDoc を追加する

### 背景
Kotlin API ガイドラインの [Library creator's guidelines — Informative documentation](https://kotlinlang.org/docs/api-guidelines-introduction.html) は、宣言の繰り返しではなく **「何を、どんなルールで、どう正規化し、何を投げるか」** を伝えるドキュメントを求めている。

iolite は Dokka でドキュメントサイト（`docs/`）を生成しているが、各 Value Object の KDoc がほぼ未記載。利用者が知りたい以下の情報がコードを読まないと分からない:

- **受理する形式**: `Email` の正規表現は何を許容して何を弾くか（プラス記号は OK か、引用符付きローカル部は？）
- **正規化処理**: `Email` は `trim().lowercase()` する、`Url` は `trim()` のみ、`DateTime` も `trim()` する、など型によってバラバラ
- **例外**: 失敗時に何がスローされるか（Issue #3 と整合）
- **境界値**: `Age` の `MIN_AGE`, `MAX_AGE`、`Url` の `MAX_URL_LENGTH = 2048` など
- **参照仕様**: `CreditCardNumber` は Valibot の実装に準拠、と README にあるがコードにはない

### 方針
- 全 VO の class 宣言と `parse()` メソッドに KDoc を付与する。
- 統一テンプレートを用意し、Konsist で「`parse()` が KDoc を持つこと」を強制する。
- README の使い方は維持しつつ、API リファレンス（Dokka 出力）を主たる仕様の置き場にする。

### 詳細

**KDoc テンプレート:**

```kotlin
/**
 * <1-2 行の概要>
 *
 * Accepts: <受理する形式の説明>
 * Normalization: <正規化処理（trim、lowercase 等）>
 *
 * @sample <パッケージ.サンプル関数>
 *
 * @see <関連 RFC / 仕様 URL があれば>
 */
@JvmInline
value class Foo(...)
```

**`parse()` の KDoc:**

```kotlin
/**
 * Validates the wrapped value and returns the normalized form.
 *
 * @return the normalized value (e.g. trimmed, lowercased)
 * @throws IoliteException if the value does not match <ルール名>
 */
override fun parse(): String { ... }
```

**実装例（Email）:**

```kotlin
/**
 * RFC 5322 に近い実用的な Email アドレス。
 *
 * Accepts:
 * - ローカル部: `A-Za-z0-9_'+-.`、ただし先頭・末尾のドット禁止、連続ドット禁止
 * - ドメイン部: ラベルが英数字とハイフンで構成され、TLD は 2 文字以上のアルファベット
 *
 * Normalization:
 * - 前後の空白を `trim()` で削除
 * - すべて小文字に正規化（`lowercase()`）
 *
 * @sample iolite.samples.emailSample
 */
@JvmInline
value class Email(private val value: String) : ValueObject<String> {

    /**
     * Validates and returns the normalized email address.
     *
     * @return the trimmed and lowercased email address
     * @throws IoliteException if the input does not match the accepted email format
     */
    override fun parse(): String { ... }
}
```

**対象ファイル（commonMain/kotlin 配下）:**

- `ValueObject.kt`（インターフェース全体の利用方針も書く）
- `personal/Email.kt`, `Age.kt`, `CreditCardNumber.kt`, `JpPhoneNumber.kt`, `JpPostalCode.kt`
- `strings/StringValueObject.kt`, `IntegerString.kt`, `DecimalString.kt`, `AlphaNumericString.kt`
- `datetime/Date.kt`, `DateTime.kt`, `Time.kt`
- `network/Url.kt`, `HostName.kt`, `Domain.kt`, `IpV4.kt`, `IpV6.kt`, `MacAddress.kt`, `Cidr.kt`
- `encoding/Base64.kt`
- `id/Uuid.kt`

合計 20 ファイル前後。

**`@sample` を使う場合:**
- `src/commonMain/kotlin/samples/` ディレクトリに `EmailSamples.kt` 等を置き、Dokka が拾えるようにする。Dokka 設定で `samples` ソースを指定。

**Konsist ルール追加（`ValueObjectRuleTest.kt`）:**

```kotlin
@Test
fun `every value class has KDoc on parse()`() {
    Konsist
        .scopeFromProject()
        .classes()
        .withValueModifier()
        .functions()
        .filter { it.name == "parse" }
        .assertTrue { it.hasKDoc }
}
```

**段階対応:**
- まずは class 宣言レベルの KDoc を全 VO に追加（PR #1）。
- 次に `parse()` メソッドレベルの KDoc を追加（PR #2）。
- 最後に Konsist ルールを有効化して回帰防止（PR #3）。

---

## Issue #7: Multiplatform 対応の方針を確定する（KMP を本気でやる / JVM 専用に畳む）

### 背景
プロジェクトは `kotlin("multiplatform")` プラグインを宣言しており、README にも KMP 対応を匂わせる記述がある。しかし実態は:

- `build.gradle.kts:21` で `jvm()` のみがターゲット
- 全 VO が `@JvmInline value class`（JVM 固有のアノテーション）
- `commonMain` / `commonTest` のソースセットを使っているが、コードは JVM のみで動く

つまり **「KMP プロジェクトの形だけ整えた純 JVM ライブラリ」** という状態。Kotlin API ガイドラインの [Introduction — Multiplatform support](https://kotlinlang.org/docs/api-guidelines-introduction.html) は、KMP を採用するならば共有コードとプラットフォーム固有コードの両方で**確実に動く** API 設計を求めている。現状は klibs.io への露出目的で KMP を選んでいるが、利用者から見ると JS/Native でも使えるという誤解を招く。

選択肢は二つ:

**A. KMP をきちんと展開する**
- JS、Native（少なくとも Linux/macOS/iOS）ターゲットを追加
- `@JvmInline` を外して expect/actual または素の `value class` に統一
- 各 VO の `Regex` 動作がプラットフォームで違わないことを検証

**B. JVM 専用に畳む**
- `kotlin("multiplatform")` を `kotlin("jvm")` に変更
- ソースセットを `src/main/kotlin` に戻す
- `@JvmInline` はそのまま
- 余計な KMP 設定（`KotlinMultiplatform` publish 設定等）を撤去

### 方針
**まず利用ニーズを確認し、いずれかに決める。** どちらを取っても今より良い状態になる。中途半端な現状が最悪。

### 詳細

**判断材料の収集:**
- GitHub Issues / Discussion で「JS/Native で使いたい」という要望があるか確認
- klibs.io 上での表示を維持したい意向はあるか
- メンテナの工数許容量

**選択肢 A（KMP 拡張）の作業内容:**

1. ターゲット追加（`build.gradle.kts`）:

```kotlin
kotlin {
    jvm { /* ... */ }
    js(IR) {
        browser()
        nodejs()
    }
    linuxX64()
    macosArm64()
    macosX64()
    iosArm64()
    iosSimulatorArm64()
}
```

2. `@JvmInline` の扱い:
   - 全 VO から `@JvmInline` を削除（純粋な `value class` でも JVM 上ではほぼ同じ最適化が効く）
   - もしくは expect/actual で JVM のみアノテーション付与
   - 推奨: 単純に外す

3. プラットフォーム差分の検証:
   - `Regex` の挙動: JVM (Java regex) と JS (ECMAScript regex) で記法が違う。例えば `\b`, look-behind, named group の対応状況。
   - `String.lowercase()` のロケール挙動: トルコ語 i 問題などプラットフォーム差なし（Kotlin 1.5+ で `lowercase()` はロケール独立）。
   - `Regex` の差分は CI で全プラットフォーム実行して確認。

4. CI に各ターゲットのビルド & テストを追加。

**選択肢 B（JVM 専用化）の作業内容:**

1. `build.gradle.kts` の plugin を変更:

```kotlin
plugins {
    kotlin("jvm") version "2.0.21"
    // 他は維持
}
```

2. ソースセット移動:
   - `src/commonMain/kotlin/*` → `src/main/kotlin/*`
   - `src/commonTest/kotlin/*` → `src/test/kotlin/*`
   - `src/jvmTest/kotlin/*` → `src/test/kotlin/*`

3. `mavenPublishing` の `KotlinMultiplatform` 設定を `KotlinJvm` に変更:

```kotlin
import com.vanniktech.maven.publish.KotlinJvm

mavenPublishing {
    configure(KotlinJvm(javadocJar = JavadocJar.Dokka("dokkaHtml"), sourcesJar = true))
    // ...
}
```

4. `coordinates` の `iolite` artifact ID を維持（公開済みの座標は変えない）。

5. README から KMP に関する暗黙の示唆を削除し、「Kotlin/JVM library」と明記。

**選択を保留する場合の暫定対応:**
- README の冒頭に「現在 JVM ターゲットのみサポート。他プラットフォームは roadmap」と明記し、誤認を防ぐ。
- これだけでもユーザー体験は改善する。

**推奨:**
- メンテナのリソースが限られるなら **B（JVM 専用化）** を推奨。シンプルになる。
- ただし KMP は klibs.io 露出と将来性のメリットが大きいため、**A** で iOS/JS だけでも対応するのは長期的に見て悪くない投資。
