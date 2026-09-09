# Session context - Locafy vendor website (`vendors.magento2.click`)

Brief for a coding session working on the **website/backend**, not the mobile app.
Hand this file's path to that session as its starting context.

## Why this work exists

The vendor Android app (`magentoegypt.locafy`, listed on Play as *Locafy
Marketplace*) has release **1.03 / versionCode 4** uploaded and staged for a full
production rollout, but **Play refuses to accept the submission**: the pre-submit
check reports *"Incomplete data safety declaration"*. Separately, Play has an open
warning - *"User Data - Account Deletion Requirement: invalid data deletion link"*
- with **"fix by Sep 21 or the app may be removed"**.

Both are fixed on the web side, not in the app. That is what this session is for.

Related material in the app repo (`C:\xampp\htdocs\locafy-vendorapp`):
- `RELEASE.md` - full store runbook and the Play policy state
- `docs/play-data-safety.md` - the declaration answers, with code evidence
- `docs/account-deletion.html` - the page to publish (content below)

## The codebase

| | |
|---|---|
| Path | `C:\xampp\htdocs\locafy_market` |
| Platform | Magento **2.4.3-p1** Community |
| Marketplace | CedCommerce `Ced_Cs*` stack (`app/code/Ced/…`) + `MageNative` modules |
| App-facing API | `app/code/Ced/VendorApi`, `app/code/MageNative/Marketplace` |
| Serves | `https://vendors.magento2.click/` |

Two sibling folders are named confusingly: `vendors-magento2click` and
`locafy-magento2click` are **Flutter apps**, not this website.

`app/code/Ced/*` and `app/code/MageNative/*` are third-party modules under a
CedCommerce EULA. Prefer plugins/preferences in a project module over editing
those files, so an upgrade doesn't wipe the change.

## Task 1 - publish the account-deletion page (deadline 21 Sep)

Play rejected the previous link because it *"does not hold reference to the entity
… named in the app's Google Play listing"*.

- Content is ready in `locafy-vendorapp/docs/account-deletion.html`. It already
  names *Locafy Marketplace* (listing name), *Locafy Seller* (device label),
  `magentoegypt.locafy`, and *Clickalize Agency* (developer). **Keep those names**
  - they are the fix.
- Publish as a CMS page at **`https://vendors.magento2.click/account-deletion`**
  (Content > Pages; URL key `account-deletion`; enabled for all store views).
- Must be publicly reachable with **no login** and must not 404, ever - if it
  breaks later, the violation returns.
- Fill the remaining placeholders before publishing: `{{SUPPORT_EMAIL}}`,
  `{{COMPANY_LEGAL_NAME}}`, `{{LAST_UPDATED}}`. (Retention is already set to
  5 years - Egyptian tax statute of limitations; have the accountant confirm.)
- Then paste the URL into Play Console > App content > Data safety.

**One claim in that page is already verified against this codebase:** deletion is
a genuine hard delete, so the page's wording is accurate. See
`Ced\VendorApi\Model\Api\Vendor\Vendor::deleteVendor()`
(`app/code/Ced/VendorApi/Model/Api/Vendor/Vendor.php:69`) - it calls
`deleteVendorProducts()`, sends the `VENDOR_DELETED_STATUS` account email, then
`$vendor->delete()`.

## Task 2 - privacy policy page

The public Play listing shows **no privacy policy**, and Play requires one in
Store settings; the Data safety form links to it. Publish one on the same domain
and hand the URL back. It must cover vendor data specifically: name, email, phone,
address, tax ID, bank details, location, photos, messages.

## Task 3 - security: `deletevendor` is unauthenticated (urgent)

While confirming Task 1 I found this, and it should not wait.

```
POST https://vendors.magento2.click/rest/V1/vendorapi/deletevendor
body: { "vendor_id": <id> }
```

- Route: `app/code/Ced/VendorApi/etc/webapi.xml:30` - `<resource ref="anonymous"/>`
- Handler: `Ced\VendorApi\Model\Api\Vendor\Vendor::deleteVendor()` - loads the
  vendor straight from `$parameters->getVendorId()` and deletes it. **No session,
  hash, or token check anywhere in the method.**
- The Android app sends nothing but `vendor_id`
  (`Ced_MultiVendor_VendorProfile.java:326`), so there is no secret being omitted.
- Vendor ids are sequential integers.

Net effect: anyone on the internet can delete any vendor account **and all of that
vendor's products** by posting an integer. The rest of this API is `anonymous` by
design - CedCommerce validates a `vendorhash` session token inside the handlers
(`Ced/VendorApi/Controller/Apiabstract.php`) - so the bug is that this one
destructive handler skips that check, not that the route is public.

Fix direction: require the caller's `vendorhash`/session token, verify it resolves
to the *same* vendor id being deleted, and reject otherwise - implemented as a
plugin on the service class so the vendor module stays untouched.

**Do not test this against production.** A successful test destroys a real vendor
and their catalogue. Reproduce on a local copy of the database only.

## Task 4 - verify the store view codes

The apps build store-scoped REST paths - `…/eg/`, `…/eg-en/`, and
`getStoreLocale()`-derived variants. If store views with exactly those codes are
missing, REST calls 404 even with a correct domain and a healthy backend. Confirm
`eg` and `eg-en` exist and are active.

## Answers the app side is waiting on

1. **Retention period** for orders, invoices and payout records after account
   deletion (tax/accounting) - goes into both the deletion page and the Data
   safety form.
2. **Support email address** to publish on the deletion page. Candidate: the app
   already shows vendors `v-relations@locafy.market` on the iOS login screen
   (`vendor_ios/VenderApp/ced_vendorLogin.swift:433`) - but that is on the old
   `locafy.market` domain, so confirm the mailbox is still monitored, or replace it
   in both places.
3. **Legal entity name** behind the Play listing.
4. Does the `VENDOR_DELETED_STATUS` account email template exist and send
   correctly? The deletion flow calls it.

## Working notes

- After CMS or config changes: `bin/magento cache:flush`, and reindex if catalog
  data was touched.
- Do not edit `vendor/` or core files.
- The app is bilingual (English + Arabic, RTL) - published pages should exist for
  both store views.
