#!/usr/bin/env bash
# Upload an APK/IPA to Loadly (https://loadly.io/doc/view/api) and publish the
# install link in the job summary.
#
#   loadly-upload.sh <file> <label>
#
# Env: LOADLY_API_KEY (required), LOADLY_BUILD_PASSWORD (optional: makes the
# install page password-protected), COMMIT_MSG + GITHUB_SHA (release notes).
set -euo pipefail

FILE="$1"
LABEL="$2"

NOTES="$(printf '%s\n' "${COMMIT_MSG:-}" | head -n 1) (${GITHUB_SHA:0:7})"

# --form-string for text fields so a leading '@' or '<' in the commit message
# is sent literally instead of being read as a file.
args=(
  --form-string "_api_key=${LOADLY_API_KEY}"
  --form-string "buildUpdateDescription=${NOTES}"
  -F "file=@${FILE}"
)
if [ -n "${LOADLY_BUILD_PASSWORD:-}" ]; then
  args+=(--form-string "buildInstallType=2" --form-string "buildPassword=${LOADLY_BUILD_PASSWORD}")
fi

echo "Uploading ${FILE} ($(du -h "$FILE" | cut -f1)) to Loadly..."
RESP=$(curl -sS --max-time 1200 "${args[@]}" https://api.loadly.io/apiv2/app/upload)

KEY=$(jq -r '.data.buildKey // empty' <<<"$RESP" 2>/dev/null || true)
if [ -z "$KEY" ]; then
  MSG=$(jq -r '"code \(.code): \(.message)"' <<<"$RESP" 2>/dev/null || printf '%s' "$RESP")
  echo "::error::Loadly upload of ${LABEL} failed: ${MSG}"
  exit 1
fi

URL="https://loadly.io/${KEY}"
VERSION=$(jq -r '.data.buildVersion // empty' <<<"$RESP")
QR=$(jq -r '.data.buildQRCodeURL // empty' <<<"$RESP")
echo "Loadly ${LABEL} ${VERSION}: ${URL}"
{
  echo "### Loadly: ${LABEL} ${VERSION}"
  echo "Install page: ${URL}"
  [ -n "$QR" ] && echo "" && echo "![QR code](${QR})"
  echo ""
  echo "Notes: ${NOTES}"
} >> "${GITHUB_STEP_SUMMARY:-/dev/null}"
