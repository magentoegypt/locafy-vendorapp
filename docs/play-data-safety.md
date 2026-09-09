# Play Data safety declaration - Locafy Marketplace (`magentoegypt.locafy`)

Answers for the Play Console Data safety questionnaire, derived from the app's
own code. Every row cites where the data comes from so the declaration can be
defended.

**Why this is being redone:** the live listing currently declares *"No data
collected"* and *"No data shared with third parties"*. That is not true of this
app - it collects vendor name, email, phone, address, tax ID, **bank details**,
location, photos and messages - and the incomplete/incorrect declaration is what
Play removed and is now blocking the 1.03 release over.

Play's definitions, which the answers below follow:
- **Collected** = transmitted off the device.
- **Shared** = transferred to another *company*. A service provider processing on
  your behalf (Firebase/Google) is not "shared"; another company using it for its
  own purposes is.
- **Required** = the user cannot use the feature without providing it, or it is
  gathered automatically with no opt-out.

## Section 1 - Data collection and security

| Question | Answer | Basis |
|---|---|---|
| Does your app collect or share any of the required user data types? | **Yes** | See section 2 |
| Is all of the user data collected by your app encrypted in transit? | **Yes** | `AppUrl.BASE_URL` and `vendorappData.plist` are `https://vendors.magento2.click/`; all REST calls derive from it. **Tighten first:** the manifest still sets `usesCleartextTraffic="true"` and `network_security_config` permits cleartext for all domains, so this answer is only honest once that is narrowed (open item 6 in RELEASE.md) |
| Do you provide a way for users to request that their data is deleted? | **Yes** | In-app: Profile -> delete account -> `POST rest/V1/vendorapi/deletevendor`, plus the web page in `docs/account-deletion.html` |
| Have you committed to follow the Families policy? | **No** | Business app for approved vendors, 18+ |
| Has your app been independently validated against a security standard? | **No** | No such review has been done |

## Section 2 - Data types

Shorthand: **C** = collected, **S** = shared, **Req** = required, **Opt** = optional.

### Personal info

| Data type | C | S | Req/Opt | Purposes | Evidence in code |
|---|---|---|---|---|---|
| Name | Yes | No | Req | App functionality, Account management | `first_name` / `last_name` in vendor registration |
| Email address | Yes | No | Req | App functionality, Account management | Login and registration payloads |
| Phone number | Yes | No | Req | App functionality, Account management | `mobile`, `mobile_number`; OTP flow in `ValidateAndSendOtp.kt` |
| Address | Yes | No | Req | App functionality | `address`, `city`, `zip_code`, `country` in registration and shipping settings |
| Other info | Yes | No | Req | App functionality | `taxvat` (tax registration number) |

### Financial info

| Data type | C | S | Req/Opt | Purposes | Evidence in code |
|---|---|---|---|---|---|
| Other financial info | Yes | No | Req | App functionality | `bank_name`, `bank_branch` in registration; payout/transaction and payment-request screens |

User payment info, purchase history and credit score: **not collected**. The app
shows the vendor their *customers'* orders, which is business data returned by the
store, not data collected from the app's user.

### Location

| Data type | C | S | Req/Opt | Purposes | Evidence in code |
|---|---|---|---|---|---|
| Approximate location | Yes | No | **Opt** | App functionality | `ACCESS_COARSE_LOCATION`; permission-gated |
| Precise location | Yes | No | **Opt** | App functionality | `ACCESS_FINE_LOCATION`; `ShippingSettingController` reverse-geocodes the device location into the warehouse / store-pickup address, which is then saved to the store |

### Photos and videos

| Data type | C | S | Req/Opt | Purposes | Evidence in code |
|---|---|---|---|---|---|
| Photos | Yes | No | Opt | App functionality | Camera and gallery pickers for product images, message and support-ticket attachments |
| Videos | No | - | - | - | No video capture or upload path found - **confirm** before answering |

### Messages

| Data type | C | S | Req/Opt | Purposes | Evidence in code |
|---|---|---|---|---|---|
| Other in-app messages | Yes | No | Req | App functionality | Admin/customer messaging and support-ticket modules |

### App activity

| Data type | C | S | Req/Opt | Purposes | Evidence in code |
|---|---|---|---|---|---|
| App interactions | Yes | No | Req | Analytics | `firebase-analytics` collects automatically with no in-app opt-out |
| Other user-generated content | Yes | No | Req | App functionality | Product titles, descriptions, prices, CMS pages and blocks the vendor creates |

### App info and performance

| Data type | C | S | Req/Opt | Purposes | Evidence in code |
|---|---|---|---|---|---|
| Crash logs | **See note** | **See note** | Opt | Diagnostics | `MyApplication` configures ACRA with `setMailTo("sajidnawaz993@gmail.com")` in TOAST mode - the report is handed to the user's own mail app and, if they send it, goes to a personal third-party address |

**Fix this before declaring.** As it stands the truthful answer is "crash logs
collected *and shared with a third party*", naming an unrelated individual. Point
ACRA at a Locafy address, or drop ACRA for Firebase Crashlytics (Firebase is
already integrated) - then it becomes "collected, not shared, Diagnostics", which
is both accurate and unremarkable.

### Device or other IDs

| Data type | C | S | Req/Opt | Purposes | Evidence in code |
|---|---|---|---|---|---|
| Device or other IDs | Yes | No | Req | App functionality, Analytics | FCM registration token sent to the store for push; Firebase Analytics app-instance ID |

**Advertising ID: not collected.** The manifest removes it explicitly:
`<uses-permission android:name="com.google.android.gms.permission.AD_ID" tools:node="remove" />`.
Declare no ads and no advertising ID.

## Section 3 - Third-party sharing

Nothing needs to be declared as *shared* **provided** two open items are closed
first:

1. **ACRA crash mail** - see the note above. Left as is, crash logs are shared
   with a third party.
2. **Facebook login** - the login screen still shows a Facebook row wired to
   `LoginManager`, using CedCommerce's template app id. If it ships enabled, data
   goes to Meta for Meta's own purposes and must be declared as shared. Hiding the
   row (already recommended, and it does not work anyway without a client token)
   keeps the answer at "not shared".

Firebase (Messaging, Analytics, Auth) and Google Sign-In are Google acting as a
service provider for you, so they are collection, not sharing.

## Determined from the backend / law

- **Deletion is a hard delete.** `Ced\VendorApi\Model\Api\Vendor\Vendor::deleteVendor()`
  (in `C:\xampp\htdocs\locafy_market`) calls `deleteVendorProducts()`, sends the
  `VENDOR_DELETED_STATUS` account email, then `$vendor->delete()`. The vendor
  account and its catalogue are removed; Magento's separate sales records
  (orders, invoices, credit memos) are not touched and are retained per tax law.
- **Retention period: 5 years** from the end of the financial year of the
  transaction - Egypt's tax statute of limitations (Income Tax Law 91/2005, per
  PwC), matched by the Commercial Code (Law 17/1999) book-retention period. The
  tax window extends to 6 years for tax evasion. Have Locafy's accountant confirm
  the start point before publishing.

## Before you submit - three things only you can confirm

1. **Support email address** for the deletion page (the app has a "Support Email"
   field but no address is baked into the app).
2. **Privacy policy URL.** The public listing shows no privacy policy; Play
   requires one in Store settings, and the Data safety form links to it.
3. **Is Facebook login staying?** Determines section 3.

## Order of work

1. Fix the ACRA crash-report address (code change, small).
2. Hide or fix Facebook login (code change, small).
3. Publish the account-deletion page - `docs/account-deletion.html` - and the
   privacy policy on a Locafy domain.
4. Complete the Data safety questionnaire with the answers above and paste the
   deletion URL into the account-deletion field.
5. Submit; the staged 1.03 production release goes with it.
