# Release runbook - Locafy Seller (vendor apps)

How to get the vendor apps onto Google Play and the App Store, what is already
prepared in the repo, and what still needs a human with console access.

Related QA: ClickUp [CL034-TC19] / test run TR01 / bug QA01.

## Where this stands on Google Play (checked 9 Sep 2026)

The Android app **is already published**. Play Console account *Clickalize Agency*
(`8055350750928047183`), app id `4972999203685551923`:

- Listed as **"Locafy Marketplace"**, package `magentoegypt.locafy`, **Production**,
  released 6 Oct 2025, **versionName 1.02 / versionCode 3**, 177 countries, 6
  installs. The store description is the vendor dashboard copy ("Empowering Fashion
  Sellers with Smart Management Tools"), so this listing is the seller app - just
  named "Marketplace".
- The repo is **behind the store**: `versionCode 2` / `versionName 1.01`. The next
  upload needs versionCode >= 4 and a higher versionName, or Play rejects it as an
  already-used version code.

Two policy problems are open on that listing, and both block the ticket:

1. **App must target Android 16 (API 36) or higher** - "app updates with these
   issues will be rejected", enforced 31 Aug 2026. **Done in the repo**, pending
   upload: `compileSdk`/`targetSdk` 36 on AGP 8.9.3 / Gradle 8.11.1 / Kotlin 2.1.0,
   version bumped to `1.03` / `versionCode 4` (the store is on 1.02 / 3). See
   "Targeting API 36" below for the behaviour changes that came with it - they need
   device QA before this ships.
2. **User Data - Account Deletion Requirement: invalid data deletion link on the
   Data safety form** - "fix by Sep 21 or the app may be removed", and the Data
   safety section has already been removed from the listing. Play's reason: the
   deletion link "does not hold reference to the entity ... named in the app's
   Google Play listing". Fix = publish an account-deletion page on a Locafy-owned,
   Locafy-branded URL and put that in the Data safety form. This one has the nearer
   deadline and is console + web work, not app work.

Also worth a decision: the Play listing says "Locafy Marketplace" while the Android
app label and (now) the iOS display name say "Locafy Seller". Pick one - the seller
naming is the clearer of the two, and the customer-facing listing
`app.locafy` ("Locafy Fashion Marketplace") already owns the marketplace name.

## What ships

| | Android | iOS |
|---|---|---|
| Identifier | `magentoegypt.locafy` | `magentoegypt.locafy` |
| Store / home-screen name | Locafy Seller | Locafy Seller |
| Version | `versionName 1.03`, `versionCode 4` (store is on 1.02 / 3) | `MARKETING_VERSION 1.01`, build `1` |
| Min OS | Android 7.0 (API 24) | iOS 15.6 (Release config) |
| Target | API 36 (AGP 8.9.3 / Gradle 8.11.1 / Kotlin 2.1.0) | Xcode 16 / latest SDK |
| Devices | phones + tablets | iPhone **and iPad** (`TARGETED_DEVICE_FAMILY = "1,2"`) |
| Backend | `https://vendors.magento2.click/` | same |

The customer app is a separate project (`com.magentoegyptpro.ajstore` /
`com.magentoegypt.ajstore`), so there is no identifier collision between the two
listings under the same developer accounts.

## Building the artifacts

**Android (AAB - the format Play requires for a new app)**

Signing credentials never live in the repo. Either create
`vendror_android/keystore.properties` (git-ignored):

```properties
storeFile=C:/path/to/upload-key.jks
storePassword=...
keyAlias=...
keyPassword=...
```

or export `LOCAFY_KEYSTORE_FILE`, `LOCAFY_KEYSTORE_PASSWORD`, `LOCAFY_KEY_ALIAS`,
`LOCAFY_KEY_PASSWORD`. Then:

```bash
./gradlew :app:bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`. With no credentials
configured the release build still runs, just unsigned.

CI does the same: add the four `ANDROID_*` repository secrets listed in
`.github/workflows/build.yml` and every push to `main` uploads a signed
`app-release-aab` artifact alongside the debug APK.

**iOS (IPA)**

CI builds a signed IPA once `BUILD_CERTIFICATE_BASE64`, `P12_PASSWORD`,
`BUILD_PROVISION_PROFILE_BASE64`, `KEYCHAIN_PASSWORD` and `APPLE_TEAM_ID` are set;
without them it produces an unsigned IPA so the pipeline stays green. Locally:
`pod install`, open `LocafyApp.xcworkspace`, scheme `VenderApp`, Product > Archive.

## Targeting API 36 - what changed and what QA must check

Play requires API 36, so the toolchain moved with it: **AGP 8.1.4 -> 8.9.3**,
**Gradle 8.9 -> 8.11.1**, **Kotlin 1.9.0 -> 2.1.0** (AGP 8.9 pulls a 2.1.0 stdlib
that the 1.9 compiler cannot read), the removed-in-modern-AGP `dexOptions` block is
gone and `lintOptions` is now `lint`. CI installs `platforms;android-36` /
`build-tools;36.0.0`.

Android 16 applies three behaviour changes to apps targeting 36. Two have opt-outs
that are still honoured and are now set in the manifest; the third does not:

- **Predictive back** is on by default and would stop `onBackPressed()` being
  called - six activities override it. Opted out with
  `android:enableOnBackInvokedCallback="false"`.
- **Orientation / resizability limits are ignored on screens >= 600dp**, so the
  portrait-only activities would become resizable on tablets (the listing covers
  tablets). Opted out with the `PROPERTY_COMPAT_ALLOW_RESTRICTED_RESIZABILITY`
  manifest property. Both opt-outs are temporary - they stop working at API 37, so
  the screens do need reworking eventually.
- **Edge-to-edge is enforced and `windowOptOutEdgeToEdgeEnforcement` is ignored on
  Android 16.** There is no opt-out, so `MyApplication` now applies the system-bar
  insets as padding to every activity's content view and paints the strips with
  `status_bar_color` (#002248), which is the colour the theme previously got from
  `colorPrimaryDark`. This is the change most likely to need visual touch-ups.

**QA this on a real Android 15 or 16 device before release:** top bars sit below the
status bar on every screen (dashboard, product list/edit, orders, chat, the drawer,
and the map/shipping screens, which are the two layouts using `fitsSystemWindows`),
the back button behaves as before, keyboard-heavy screens still scroll correctly,
and the app stays portrait on a tablet.

## Store-review fixes already applied

These were live problems in the submission path and are fixed in the repo:

- Placeholder permission strings (`NSCameraUsageDescription` = "NEED PERMISSION")
  replaced with real explanations - a certain App Review rejection under 5.1.1.
- `NSAppleMusicUsageDescription` removed; nothing in the app touches the media
  library.
- `UIBackgroundModes` trimmed to `remote-notification`. `processing` and `fetch`
  were declared with no `BGTaskScheduler` or background-fetch code behind them
  (guideline 2.5.4), and the now-pointless `BGTaskSchedulerPermittedIdentifiers`
  went with them.
- Location changed from `requestAlwaysAuthorization()` to
  `requestWhenInUseAuthorization()` in `ShippingSettingController`. The warehouse
  address picker only needs location while that screen is open; "Always" without a
  background use case gets queried or rejected.
- `UIRequiredDeviceCapabilities` `armv7` -> `arm64`. The binary is 64-bit only, and
  the stale armv7 capability fails App Store Connect bundle validation.
- App Transport Security: blanket `NSAllowsArbitraryLoads` replaced with a scoped
  exception for `magento2.click`. Every request the app makes is built from the
  HTTPS base URL; the exception only covers media the Magento instance might still
  serve over HTTP.
- iOS product and display name changed from "Locafy Marketplace" to "Locafy
  Seller", matching the Android label and the Xcode scheme. A seller app showing
  the marketplace name confuses reviewers and vendors alike.
- `ITSAppUsesNonExemptEncryption = false` declared (HTTPS only), so App Store
  Connect stops asking on every upload.
- Android release `signingConfig` wired up, plus a CI job for the signed AAB.

## Open items - these need a decision or console access

1. **Rotate the signing key.** `vendror_android/masterpieceseller.jks` and a file
   literally named `password for jks` are committed and are in git history. Do not
   publish with that key: generate a fresh upload key, enrol the app in Play App
   Signing, and delete both files from the repo (rewriting history is a separate
   decision).
2. **Crash reports go to a stranger.** `MyApplication` configures ACRA to email
   crash reports to `sajidnawaz993@gmail.com`, a previous developer's personal
   Gmail. Point it at a Locafy address, or drop ACRA in favour of Firebase
   Crashlytics - Firebase is already integrated. This also has to be answered
   truthfully in the Play Data safety form.
3. **Facebook and Google sign-in are live on the Android login screen.** Both rows
   are visible in `activity_ced__multivendor__new__login.xml`, and
   `Ced_Multivendor_New_Login` wires them up - the Facebook row forwards its click
   to the hidden `LoginButton`. Two things to settle before reviewers touch them:
   - Facebook: the manifest carries CedCommerce's template app id
     `4575198272598318` and no `com.facebook.sdk.ClientToken` meta-data, which
     Facebook SDK 12 requires. As it stands this login will fail.
   - Google: after enrolling in Play App Signing, the SHA-1 that matters is Play's
     app-signing certificate, not the upload key. Add it to the Firebase project or
     Google sign-in breaks only in the published build.

   Either configure Locafy's own Facebook app and OAuth clients and test both, or
   hide the two rows for this release. A broken login button on the first screen is
   a guaranteed rejection. (iOS has both providers commented out, so Sign in with
   Apple - guideline 4.8 - is not triggered there.)
4. **iPad or not.** `TARGETED_DEVICE_FAMILY = "1,2"` means Apple expects the app to
   work on iPad and the listing needs iPad screenshots. The UI is portrait iPhone
   XIBs. Either test and screenshot on iPad, or set the family to `1`.
5. **Apple signing settings point at ad-hoc.** Debug uses team `Z83RXFCWNH`
   (automatic signing); Release uses `544Y9RU66L` with
   `PROVISIONING_PROFILE_SPECIFIER[sdk=iphoneos*] = locafy_adhoc`. CI overrides both
   from `APPLE_TEAM_ID` and the profile in the secrets, but a local Product > Archive
   produces an ad-hoc build, not an App Store one. Point the release config at the
   team and distribution profile that actually own the listing.
6. **Android cleartext traffic.** `usesCleartextTraffic="true"` plus a
   `network_security_config` whose base config permits cleartext for every domain.
   The backend is HTTPS; the only cleartext consumer found is the membership-plan
   WebView (`Ced_Weblink`), which prepends `http://` to bare links. Narrow the
   config once that link handling is confirmed.
7. **Legacy permissions.** `GET_ACCOUNTS`, `USE_CREDENTIALS`, `GET_TASKS` and
   `REAL_GET_TASKS` are declared. Drop the ones the app does not use - unexplained
   permissions invite Play policy review.
8. **Camera is a hard requirement.** `<uses-feature android:name="android.hardware.camera"
   android:required="true" />` hides the app from camera-less devices. Set
   `required="false"` unless that is intentional.

## Google Play submission

This is an **update to the existing listing**, not a new app - see the Play status
section above. Order of work: clear the account-deletion violation (deadline
21 Sep), move to `targetSdk 36`, bump the version, then release.

- Upload the signed AAB to Internal testing first - the fastest way to prove the
  artifact and the vendor demo account work end to end.
- **App access:** the whole app sits behind vendor approval, so reviewers cannot
  sign themselves up. Put the demo vendor credentials here or the review stalls.
- **Data safety:** declare what the app actually does - account data (vendor
  login), photos taken and uploaded for products, approximate and precise location
  for the warehouse address, plus whatever Firebase Analytics, the Facebook SDK and
  the crash reporter collect. Keep this in step with open items 2 and 3.
- **Ads:** the app removes the advertising id (`AD_ID` is `tools:node="remove"`), so
  declare no ads and no advertising id.
- Content rating questionnaire, privacy policy URL (can be shared with the customer
  app if it covers vendor data too), target audience 18+.
- Countries: Egypt at minimum; add others if vendors operate there.

## App Store submission

- App Store Connect > new app, bundle id `magentoegypt.locafy`, SKU, primary
  language, **Business** category.
- Upload from Xcode Organizer, or the CI IPA via Transporter.
- **App Review Information** must carry the demo vendor account and a note about
  the restricted audience - template below.
- App Privacy questionnaire, mirroring the Play Data safety answers.
- Screenshots for 6.9" and 6.5" iPhone, plus iPad if open item 4 keeps iPad.

### Review notes template

```
Locafy Seller is the vendor-side companion app for the Locafy marketplace
(https://vendors.magento2.click). Approved marketplace sellers use it to manage
their shop: products, orders, shipments, invoices, payouts, reviews and support
tickets.

The app is not intended for shoppers and offers no self-registration to the general
public - vendor accounts are approved by Locafy staff before they can sign in.
Please use the demo vendor account below; it is preloaded with products and orders
so every screen can be exercised.

    Email:    <demo vendor email>
    Password: <demo vendor password>

Shoppers use our separate customer app; this listing is the seller counterpart.
```

## Before you submit - verification

- [ ] Signed AAB / IPA installs on a clean physical device.
- [ ] **Android 15/16 device:** no screen draws under the status or navigation bar,
      back button still works, app stays portrait on a tablet (see "Targeting API 36").
- [ ] Demo vendor account signs in **on the release build**, not just debug.
- [ ] Core vendor flows work: login, dashboard, add and edit product, order list
      and order detail, shipment, messages.
- [ ] Push notification arrives (Firebase; `remote-notification` is the only
      background mode left).
- [ ] Camera and photo-library prompts show the new purpose strings.
- [ ] Location prompt appears on shipping settings and is the "while using the app"
      variant.
- [ ] Requests go to `https://vendors.magento2.click/` - no staging host, no
      localhost.
- [ ] Arabic and English both render correctly, RTL included.

## After approval

Install from the store on a clean device, sign in with the demo vendor account,
re-run the flow list above, then close the QA test run.
