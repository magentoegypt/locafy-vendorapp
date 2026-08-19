package magentoegypt.locafy.manage_products_section;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import magentoegypt.locafy.R;

/**
 * Renders the markup of a product description field in a WebView so the images and videos embedded
 * in it are visible while editing.
 *
 * <p>Description attributes are edited as raw markup on purpose: converting them to plain text
 * drops every Page Builder wrapper plus the {@code <img>}/{@code <video>} tags, and the flattened
 * value is what gets posted back on save. Keeping the markup intact costs readability, which this
 * preview gives back.</p>
 */
public final class DescriptionMediaPreview {

    /** How long to wait after the last keystroke before re-rendering an open preview. */
    private static final long REFRESH_DELAY_MS = 700;

    private DescriptionMediaPreview() {
    }

    /**
     * Wires {@code toggle} to show and hide {@code preview}, rendering whatever {@code source}
     * currently holds. The toggle stays hidden while the value carries no markup.
     *
     * @param baseUrl store base URL, used to resolve relative asset paths and Magento directives
     */
    public static void bind(final EditText source, final TextView toggle, final WebView preview,
                            final String baseUrl) {
        if (source == null || toggle == null || preview == null) {
            return;
        }
        WebSettings settings = preview.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        preview.setWebViewClient(new WebViewClient());

        toggle.setVisibility(hasMarkup(source.getText().toString()) ? View.VISIBLE : View.GONE);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preview.getVisibility() == View.VISIBLE) {
                    preview.setVisibility(View.GONE);
                    preview.loadUrl("about:blank");
                    toggle.setText(R.string.show_media_preview);
                } else {
                    preview.setVisibility(View.VISIBLE);
                    toggle.setText(R.string.hide_media_preview);
                    render(preview, source.getText().toString(), baseUrl);
                }
            }
        });

        final Handler refreshHandler = new Handler(Looper.getMainLooper());
        source.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                final String value = editable.toString();
                toggle.setVisibility(hasMarkup(value) ? View.VISIBLE : View.GONE);
                refreshHandler.removeCallbacksAndMessages(null);
                if (preview.getVisibility() != View.VISIBLE) {
                    return;
                }
                refreshHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        render(preview, value, baseUrl);
                    }
                }, REFRESH_DELAY_MS);
            }
        });
    }

    /** True when the value carries markup or a Magento directive worth previewing. */
    public static boolean hasMarkup(String value) {
        return value != null && (value.contains("<") || value.contains("{{"));
    }

    private static void render(WebView preview, String markup, String baseUrl) {
        String html = "<!doctype html><html><head><meta charset=\"utf-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
                + "<style>body{margin:0;padding:8px;font-family:sans-serif;font-size:13px;word-wrap:break-word}"
                + "img,video,iframe,table{max-width:100%!important;height:auto}</style></head>"
                + "<body dir=\"auto\">" + resolveDirectives(markup, baseUrl) + "</body></html>";
        preview.loadDataWithBaseURL(baseUrl, html, "text/html", "utf-8", null);
    }

    /**
     * Expands the {@code {{media url=...}}} / {@code {{store url=...}}} directives Magento keeps in
     * WYSIWYG and Page Builder content, which stay unresolved outside the storefront renderer.
     */
    static String resolveDirectives(String markup, String baseUrl) {
        if (markup == null) {
            return "";
        }
        if (!markup.contains("{{")) {
            return markup;
        }
        String storeBase = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        /* Media is served from the host root, not from the store-code path. */
        String mediaBase = storeBase;
        int lastSegment = storeBase.lastIndexOf('/', storeBase.length() - 2);
        if (lastSegment > storeBase.indexOf("://") + 2) {
            mediaBase = storeBase.substring(0, lastSegment + 1);
        }
        mediaBase = mediaBase + "media/";

        StringBuilder resolved = new StringBuilder(markup.length());
        int cursor = 0;
        while (cursor < markup.length()) {
            int open = markup.indexOf("{{", cursor);
            int close = open < 0 ? -1 : markup.indexOf("}}", open);
            if (open < 0 || close < 0) {
                resolved.append(markup.substring(cursor));
                break;
            }
            resolved.append(markup, cursor, open);
            String directive = markup.substring(open + 2, close).trim();
            if (directive.startsWith("media ")) {
                resolved.append(mediaBase).append(directiveUrl(directive));
            } else if (directive.startsWith("store ")) {
                resolved.append(storeBase).append(directiveUrl(directive));
            } else {
                resolved.append(markup, open, close + 2);
            }
            cursor = close + 2;
        }
        return resolved.toString();
    }

    /** Pulls the {@code url=...} value, quoted or not, out of a Magento directive body. */
    private static String directiveUrl(String directive) {
        int equals = directive.indexOf('=');
        if (equals < 0) {
            return "";
        }
        String url = directive.substring(equals + 1).trim();
        if (url.length() > 1) {
            char first = url.charAt(0);
            if (first == url.charAt(url.length() - 1) && (first == '"' || first == 0x27)) {
                url = url.substring(1, url.length() - 1);
            }
        }
        return url;
    }
}
