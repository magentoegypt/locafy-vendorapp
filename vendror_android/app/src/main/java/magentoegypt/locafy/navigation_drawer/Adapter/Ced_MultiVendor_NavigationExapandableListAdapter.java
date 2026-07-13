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

package magentoegypt.locafy.navigation_drawer.Adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import magentoegypt.locafy.base_app.Ced_MultiVendor_FontSetting;
import magentoegypt.locafy.base_app.Ced_MultiVendor_VendorFunctionalityList;
import magentoegypt.locafy.navigation_drawer.models.Ced_MultiVendor_NavDrawerItem;
import magentoegypt.locafy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Ced_MultiVendor_NavigationExapandableListAdapter extends BaseExpandableListAdapter {

    private final Context _context;
    ArrayList<Ced_MultiVendor_NavDrawerItem> navDrawerItems;
    Ced_MultiVendor_FontSetting fontSetting;
    String current = "";
    String currentclick = "";
    Ced_MultiVendor_VendorFunctionalityList functionalityList;
    private ArrayList<String> _listDataHeader;
    private HashMap<String, ArrayList<String>> _listDataChild;

    public Ced_MultiVendor_NavigationExapandableListAdapter(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<String>> listChildData, ArrayList<Ced_MultiVendor_NavDrawerItem> navDrawerItems) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.navDrawerItems = navDrawerItems;
        fontSetting = new Ced_MultiVendor_FontSetting();
        functionalityList = new Ced_MultiVendor_VendorFunctionalityList(this._context);
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = Objects.requireNonNull(am).getRunningTasks(1);
        current = Objects.requireNonNull(taskInfo.get(0).topActivity).getClassName();
        if (context.getResources().getString(R.string.Enable_Log).equals("yes")) {
            Log.i("currentactivity", "" + current);
        }
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition))).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(infalInflater).inflate(R.layout.ced_multivendor_list_item, null);
        }
        TextView txtListChild = convertView.findViewById(R.id.MultiVendor_lblListItem);
        fontSetting.setFontforTextviews(txtListChild, "Roboto-Medium.ttf", _context);
        txtListChild.setText(" - " + childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (this._listDataChild.containsKey(this._listDataHeader.get(groupPosition))) {
            return Math.max(this._listDataChild.get(this._listDataHeader.get(groupPosition)).size(), 0);
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(infalInflater).inflate(R.layout.ced_multivendor_list_group, null);
        }
        TextView lblListHeader = convertView.findViewById(R.id.MultiVendor_lblListHeader);
        TextView selector = convertView.findViewById(R.id.MultiVendor_selector);
        ImageView icon = convertView.findViewById(R.id.MultiVendor_icon);
        ImageView identity = convertView.findViewById(R.id.MultiVendor_identity);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        fontSetting.setFontforTextviews(lblListHeader, "Roboto-Medium.ttf", _context);
        try {
            identity.setImageResource(navDrawerItems.get(groupPosition).getIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getChildrenCount(groupPosition) > 0) {
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}