# Locafy Seller — Vendor Apps

Native **iOS** and **Android** seller/vendor apps for **Locafy**, an Egypt-based
online marketplace. They are the mobile companion for a **Magento 2** store running
the **CedCommerce Multi-Vendor Marketplace (MVM)** extension, letting vendors manage
their shop — products, orders, shipments, payments, reviews, and more — from a phone.

The apps are bilingual (**English + Arabic**) and talk to the store's REST API at
**`https://vendors.magento2.click/`**.

> The apps are white-labeled from CedCommerce's "MageNative" vendor-app template
> (classes are prefixed `Ced_MultiVendor_*`). The source is covered by CedCommerce's
> proprietary EULA — it is **not** open source.

## Repository layout

| Path | Platform | Stack |
|------|----------|-------|
| [`vendor_ios/`](vendor_ios) | iOS | Swift (UIKit, `.xib`/storyboards), CocoaPods, Xcode workspace |
| [`vendror_android/`](vendror_android) | Android | Java + Kotlin, Gradle, ViewBinding/DataBinding |

_(Note: the Android folder name is spelled `vendror_android`.)_

## Backend

Both apps target the same Magento 2 + CedCommerce MVM backend:

- **Base URL:** `https://vendors.magento2.click/`
- **REST base:** `<base>/rest/V1/…`; some legacy endpoints use `<base>/vendorapi/…`
- **Store-scoped paths:** requests are store-view scoped, e.g. `…/eg/` (default) and
  `…/<storeCode>/` / `…/eg-en/`. ⚠️ The Magento instance **must** have store views with
  those exact codes or REST calls will 404 even with the correct domain.

Where the base URL lives, if it ever changes again:

- **iOS:** `vendor_ios/VenderApp/appSettings.swift` (`settings.baseUrl`) and the
  `baseUrl` key in `vendor_ios/VenderApp/vendorappData.plist`
- **Android:** `AppUrl.BASE_URL`, `Ced_MultiVendor_VendorSessionManagement.getBase_Url()`,
  and `res/values/strings.xml` (`base_url`)

## Android — build & run

**Requirements**

- JDK 17
- Android SDK with **platform 35** and **build-tools 35.0.0**
- Gradle is provided via the wrapper (Gradle 8.9)

**Setup**

1. Create `vendror_android/local.properties` pointing at your SDK (this file is
   git-ignored):
   ```properties
   sdk.dir=C\:\\Android\\Sdk        # Windows
   # sdk.dir=/Users/you/Library/Android/sdk   # macOS
   ```
2. Build a debug APK:
   ```bash
   cd vendror_android
   ./gradlew :app:assembleDebug        # Windows: .\gradlew.bat :app:assembleDebug
   ```
   Output: `app/build/outputs/apk/debug/app-debug.apk`

- **Package:** `magentoegypt.locafy` · **label:** "Locafy Seller"
- `compileSdk 35`, `minSdk 24`, `targetSdk 35`
- Uses Firebase (Messaging/Auth/Analytics), Google Play Services/Maps, Retrofit, Glide

> Installing on some devices (e.g. Xiaomi/HyperOS) requires enabling **Developer options →
> Install via USB**; sideloaded debug APKs may also need "Install anyway" past the security scan.

## iOS — build & run

**Requirements**

- macOS + Xcode
- CocoaPods
- Deployment target: iOS 13.0

**Setup**

```bash
cd vendor_ios
pod install
open LocafyApp.xcworkspace   # always open the .xcworkspace, not the .xcodeproj
```

Then select the `LocafyApp` scheme and build/run from Xcode.

## Features

Vendor login & multi-step registration (with approval flow) · dashboard · product
management (simple / configurable / bundle / grouped / downloadable, attributes,
custom options, up/cross-sell, category mapping) · orders, invoices, shipments &
credit memos · transactions & payment requests · reports · deals · CMS pages &
static blocks · product reviews & ratings · RMA · support tickets · admin/customer
messaging · auctions · membership plans · request-for-quote · store pickup (maps) ·
shipping settings.

## Notes

- **Signing secrets:** the Android signing keystore and its password were committed to
  history early on. These should be removed from version control and rotated.
- `local.properties` (Android) and Xcode `xcuserdata`/`.DS_Store` (iOS) are git-ignored.
