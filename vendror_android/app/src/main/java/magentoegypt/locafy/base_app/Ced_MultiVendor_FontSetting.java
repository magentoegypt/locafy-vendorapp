/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package magentoegypt.locafy.base_app;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Ced_MultiVendor_FontSetting {
    public void setFontforTextviews(TextView view, String font, Context context) {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/" + font);
        view.setTypeface(avnbook);
    }

    public void setfontforButtons(Button button, String font, Context context) {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/" + font);
        button.setTypeface(avnbook);
    }

    public void setfontforEditText(EditText editText, String font, Context context) {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/" + font);
        editText.setTypeface(avnbook);
    }

    public void setfontforCheckbox(CheckBox checkBox, String font, Context context) {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/" + font);
        checkBox.setTypeface(avnbook);
    }
}
