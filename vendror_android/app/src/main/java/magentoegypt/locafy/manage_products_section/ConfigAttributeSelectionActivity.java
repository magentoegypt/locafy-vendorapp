package magentoegypt.locafy.manage_products_section;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import magentoegypt.locafy.api_request_response_section.Ced_MultiVendor_ClientRequestResponse;
import magentoegypt.locafy.R;
import magentoegypt.locafy_constant.Ced_MultiVendor_GlobalVariables;
import magentoegypt.locafy.gallary.ImagePickerManager;
import magentoegypt.locafy.manage_products_section.model.AttributeModelConfig;
import magentoegypt.locafy.navigation_drawer.Activity.Ced_MultiVendor_NavigationActivity;
import magentoegypt.locafy.vendor_session.Ced_MultiVendor_VendorSessionManagement;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigAttributeSelectionActivity extends Ced_MultiVendor_NavigationActivity {

    private TextView topLabel;
    private LinearLayout groupedProductView;
    private Button backButton;
    private Button nextButton;
    private List<AttributeModelConfig> attributeModelList = new ArrayList<>();
    static List<AttributeModelConfig> attributeModelListSelected = new ArrayList<>();
    static List<AttributeModelConfig> attributeModelListFinal = new ArrayList<>();
    static List<AttributeModelConfig> attributeModelEditListFinal = new ArrayList<>();
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_attribute_selection);

        // Initialize views
        topLabel = findViewById(R.id.topLabel);
        groupedProductView = findViewById(R.id.groupedProductView);
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);

        String dataforproductcreation = getIntent().getStringExtra("dataforproductcreation");
        JSONArray config;
        try {
            JSONObject object = new JSONObject(dataforproductcreation);
            JSONArray attributes = object.getJSONObject("data").getJSONArray("attributes");
            config = object.getJSONObject("data").getJSONArray("config");
            for (int index = 0; index < config.length(); index++) {
                JSONObject jsonObject = config.getJSONObject(index);
                String attributeCode = jsonObject.getString("attribute_code");

                AttributeModelConfig existingConfig = findAttributeByCode(attributeCode);
                if (existingConfig != null) {
                    attributeModelList.add(existingConfig);
                } else {
                    AttributeModelConfig newConfig = new AttributeModelConfig();
                    newConfig.setTitle(jsonObject.getString("title"));
                    newConfig.setAttributeCode(attributeCode);
                    newConfig.setAttributeId(jsonObject.getString("value"));
                    newConfig.setAttributeLabel(jsonObject.getString("label"));
                    for(int j = 0; j < attributes.length(); j++) {
                        JSONObject attribute = attributes.getJSONObject(j);
                        if(attribute.getString("name").equalsIgnoreCase(attributeCode)){
                            JSONArray attributeArray = attribute.getJSONArray("values");
                            for (int i = 0; i < attributeArray.length(); i++) {
                                JSONObject value = attributeArray.getJSONObject(i);
                                if (!value.getString("label").isEmpty() && !value.getString("value").isEmpty()) {
                                    AttributeModelConfig childConfig = new AttributeModelConfig();
                                    childConfig.setLabel(value.getString("label"));
                                    childConfig.setValue(value.getString("value"));
                                    childConfig.setTitle(newConfig.getTitle());
                                    childConfig.setAttributeCode(newConfig.getAttributeCode());
                                    childConfig.setAttributeId(newConfig.getAttributeId());
                                    childConfig.setAttributeLabel(newConfig.getAttributeLabel());
                                    newConfig.getAttributeArray().add(childConfig);
                                }
                            }
                        }
                    }
                    attributeModelList.add(newConfig);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set click listeners
        backButton.setOnClickListener(v -> onBackButtonClicked());
        nextButton.setOnClickListener(v -> onNextButtonClicked());
        // Reverse the list if needed
         Collections.reverse(attributeModelList);
        updateUI();
    }

    private AttributeModelConfig findAttributeByCode(String code) {
        for (AttributeModelConfig config : ConfigAttributeSelectionActivity.attributeModelListSelected) {
            if (code.equals(config.getAttributeCode())) {
                return config;
            }
        }
        return null;
    }

    private void updateUI() {
        groupedProductView.removeAllViews();
        int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
       if(stepCount == 0){
           // Set initial data
           topLabel.setText(R.string.step_1_select_attributes);
           for (int i = 0; i < attributeModelList.size(); i++) {
               AttributeModelConfig attributeModelConfig = attributeModelList.get(i);
               final CheckBox checkBox = new CheckBox(getApplicationContext());
               checkBox.setText(attributeModelConfig.getTitle());
               checkBox.setTextColor(getResources().getColor(R.color.black));
               checkBox.setButtonDrawable(id);
               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                       LinearLayout.LayoutParams.WRAP_CONTENT,
                       LinearLayout.LayoutParams.WRAP_CONTENT
               );
             //  params.setMarginStart(16); // Use marginStart instead of marginLeft
               checkBox.setLayoutParams(params);
               checkBox.setChecked(attributeModelConfig.isChecked());
               checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                   attributeModelConfig.setChecked(isChecked);
               });
               groupedProductView.addView(checkBox);
           }
       }else if (stepCount == 1) {
           // Step 2: Attribute Values
           topLabel.setText(R.string.step_2);
           for (AttributeModelConfig mainConfig:ConfigAttributeSelectionActivity.attributeModelListSelected){
               // Add title view with select/deselect buttons
               View titleView = LayoutInflater.from(this).inflate(R.layout.item_attribute_title, null);
               TextView titleText = titleView.findViewById(R.id.titleLabel);
               ImageButton deleteButton = titleView.findViewById(R.id.deleteButton);
               Button selectButton = titleView.findViewById(R.id.selectButton);
               Button deselectButton = titleView.findViewById(R.id.deSelectButton);
               titleText.setText(mainConfig.getTitle());
               deleteButton.setOnClickListener(v -> {
                   ConfigAttributeSelectionActivity.attributeModelListSelected.remove(mainConfig);
                   updateUI();
               });
               selectButton.setOnClickListener(v -> {
                   for (AttributeModelConfig child : mainConfig.getAttributeArray()) {
                       child.setChecked(true);
                   }
                   updateUI();
               });
               deselectButton.setOnClickListener(v -> {
                   for (AttributeModelConfig child : mainConfig.getAttributeArray()) {
                       child.setChecked(false);
                   }
                   updateUI();
               });
               groupedProductView.addView(titleView);
               // Add checkboxes for each attribute value
               for (int i = 0; i < mainConfig.getAttributeArray().size(); i++) {
                   AttributeModelConfig child = mainConfig.getAttributeArray().get(i);
                   if(child.isUserCustom()){
                       View layout = View.inflate(this, R.layout.item_attribute_option, null);
                       CheckBox checkBox = layout.findViewById(R.id.checkboxOption);
                       ImageView iconEdit = layout.findViewById(R.id.iconEdit);
                       ImageView iconDelete = layout.findViewById(R.id.iconDelete);
                       checkBox.setText(child.getLabel());
                       checkBox.setChecked(child.isChecked());
                       checkBox.setTextColor(getResources().getColor(R.color.black));
                       checkBox.setButtonDrawable(id);
                       checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                           child.setChecked(isChecked);
                       });
                       int finalI = i;
                       iconEdit.setOnClickListener(v -> {
                           showInputDialog(getString(R.string.alert_name), getString(R.string.please_enter_the_new_value),
                                   getString(R.string.add), getString(R.string.cancel), getString(R.string.new_attribute_value),child.getLabel(),
                                   input -> {
                                       child.setLabel(input);
                                       checkBox.setText(child.getLabel());
                                       saveEditAttribute(mainConfig,child, finalI,0);
                                      // updateUI();
                                   });
                       });

                       iconDelete.setOnClickListener(v -> {
                           saveEditAttribute(mainConfig,child, finalI,finalI);
                       });
                       groupedProductView.addView(layout);
                   }else{
                       final CheckBox checkBox = new CheckBox(getApplicationContext());
                       LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                               LinearLayout.LayoutParams.WRAP_CONTENT,
                               LinearLayout.LayoutParams.WRAP_CONTENT
                       );
                       checkBox.setLayoutParams(params);
                       checkBox.setText(child.getLabel());
                       checkBox.setChecked(child.isChecked());
                       checkBox.setTextColor(getResources().getColor(R.color.black));
                       checkBox.setButtonDrawable(id);
                       checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                           child.setChecked(isChecked);
                       });
                       groupedProductView.addView(checkBox);
                   }

               }
               // Add "Create New Value" button
               Button addButton = new Button(this);
               addButton.setText(R.string.create_new_value);
               addButton.setOnClickListener(v -> {
                   showInputDialog(getString(R.string.alert_name), getString(R.string.please_enter_the_new_value),
                           getString(R.string.add), getString(R.string.cancel), getString(R.string.new_attribute_value),"",
                           input -> {
                               AttributeModelConfig newConfig = new AttributeModelConfig();
                               newConfig.setLabel(input);
                               newConfig.setUserCustom(true);
                               newConfig.setAttributeId(mainConfig.getAttributeId());
                               newConfig.setAttributeLabel(mainConfig.getAttributeLabel());
                               // Save this new attribute
                               saveEditAttribute(mainConfig,newConfig,-1,-1);
                              // mainConfig.getAttributeArray().add(newConfig);
                              // updateUI();
                           });
               });
               groupedProductView.addView(addButton);
           }
       }else if(stepCount == 2){
           topLabel.setText(R.string.step_3);
           View sku_options_layout = LayoutInflater.from(this).inflate(R.layout.sku_options_layout, null);

           groupedProductView.addView(sku_options_layout);
           RadioGroup imageRadioGroup = sku_options_layout.findViewById(R.id.imageRadioGroup);
           Spinner imageAttributeSpinner = sku_options_layout.findViewById(R.id.imageAttributeSpinner);
           LinearLayout imageAttributeContainer = sku_options_layout.findViewById(R.id.imageAttributeContainer);
           RadioGroup priceRadioGroup = sku_options_layout.findViewById(R.id.priceRadioGroup);
           Spinner priceAttributeSpinner = sku_options_layout.findViewById(R.id.priceAttributeSpinner);
           LinearLayout priceAttributeContainer = sku_options_layout.findViewById(R.id.priceAttributeContainer);
           RadioGroup quantityRadioGroup = sku_options_layout.findViewById(R.id.quantityRadioGroup);
           Spinner quantityAttributeSpinner = sku_options_layout.findViewById(R.id.quantityAttributeSpinner);
           LinearLayout quantityAttributeContainer = sku_options_layout.findViewById(R.id.quantityAttributeContainer);
           // Example items for the dropdown
           List<String> nameList = new ArrayList<>();
           nameList.add(getString(R.string.select));
           for (AttributeModelConfig config:ConfigAttributeSelectionActivity.attributeModelListSelected){
               nameList.add(config.getTitle());
           }
           ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nameList);
           imageAttributeSpinner.setAdapter(adapter);
           priceAttributeSpinner.setAdapter(adapter);
           quantityAttributeSpinner.setAdapter(adapter);

           // Dropdown selection listener
           imageAttributeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   for (AttributeModelConfig config:ConfigAttributeSelectionActivity.attributeModelListSelected){
                       config.setThirdtabImageAttributeIndex(position);
                   }
                   imageAttributeContainer.removeAllViews(); // Clear old fields
                   if(position != 0) {
                       addImageView(ConfigAttributeSelectionActivity.attributeModelListSelected.get(position - 1), false, imageAttributeContainer);
                   }
               }
               @Override
               public void onNothingSelected(AdapterView<?> parent) {}
           });
           priceAttributeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   for (AttributeModelConfig config:ConfigAttributeSelectionActivity.attributeModelListSelected){
                       config.setThirdtabPriceAttributeIndex(position);
                   }
                   priceAttributeContainer.removeAllViews(); // Clear old fields
                   if(position != 0) {
                       addEditText(ConfigAttributeSelectionActivity.attributeModelListSelected.get(position - 1), true,false, priceAttributeContainer);
                   }
               }
               @Override
               public void onNothingSelected(AdapterView<?> parent) {}
           });
           quantityAttributeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   for (AttributeModelConfig config:ConfigAttributeSelectionActivity.attributeModelListSelected){
                       config.setThirdtabQuantityAttributeIndex(position);
                   }
                   quantityAttributeContainer.removeAllViews(); // Clear old fields
                   if(position != 0) {
                       addEditText(ConfigAttributeSelectionActivity.attributeModelListSelected.get(position - 1), false, false, quantityAttributeContainer);
                   }
               }
               @Override
               public void onNothingSelected(AdapterView<?> parent) {}
           });

           imageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup group, int checkedId) {
                   String selectedOption = "Skip";
                   imageAttributeSpinner.setVisibility(View.GONE);
                   imageAttributeContainer.setVisibility(View.GONE);
                   sku_options_layout.findViewById(R.id.imageAttributeText).setVisibility(View.GONE);
                   if (checkedId == R.id.radioImageSingle) {
                       selectedOption = "Single";
                       addImageView(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0),true,imageAttributeContainer);
                   }else if (checkedId == R.id.radioImageAttribute) {
                       imageAttributeSpinner.setVisibility(View.VISIBLE);
                       sku_options_layout.findViewById(R.id.imageAttributeText).setVisibility(View.VISIBLE);
                       selectedOption = "ApplyAttribute";
                       if(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabImageAttributeIndex() != 0){
                           imageAttributeSpinner.setSelection(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabImageAttributeIndex());
                           addImageView(ConfigAttributeSelectionActivity.attributeModelListSelected.get(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabImageAttributeIndex()-1),true,imageAttributeContainer);
                       }
                   }
                   for (AttributeModelConfig config:ConfigAttributeSelectionActivity.attributeModelListSelected){
                       config.setThirdtabImageOption(selectedOption);
                   }
               }
           });
           priceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup group, int checkedId) {
                   String selectedOption = "Skip";
                   priceAttributeSpinner.setVisibility(View.GONE);
                   priceAttributeContainer.setVisibility(View.GONE);
                   sku_options_layout.findViewById(R.id.priceAttributeText).setVisibility(View.GONE);
                   if (checkedId == R.id.radioPriceSingle) {
                       selectedOption = "Single";
                       addEditText(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0),true,true,priceAttributeContainer);
                   }else if (checkedId == R.id.radioPriceAttribute) {
                       priceAttributeSpinner.setVisibility(View.VISIBLE);
                       sku_options_layout.findViewById(R.id.priceAttributeText).setVisibility(View.VISIBLE);
                       selectedOption = "ApplyAttribute";
                       if(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabPriceAttributeIndex() != 0){
                           priceAttributeSpinner.setSelection(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabPriceAttributeIndex());
                           addEditText(ConfigAttributeSelectionActivity.attributeModelListSelected.get(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabPriceAttributeIndex()-1),true,false,priceAttributeContainer);
                       }
                   }
                   for (AttributeModelConfig config:ConfigAttributeSelectionActivity.attributeModelListSelected){
                       config.setThirdtabPriceOption(selectedOption);
                   }
               }
           });
           quantityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup group, int checkedId) {
                   String selectedOption = "Skip";
                   quantityAttributeSpinner.setVisibility(View.GONE);
                   quantityAttributeContainer.setVisibility(View.GONE);
                   sku_options_layout.findViewById(R.id.quantityAttributeText).setVisibility(View.GONE);
                   if (checkedId == R.id.radioQuantitySingle) {
                       quantityAttributeContainer.setVisibility(View.VISIBLE);
                       selectedOption = "Single";
                       addEditText(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0),false,true,quantityAttributeContainer);
                   }else if (checkedId == R.id.radioQuantityAttribute) {
                       quantityAttributeSpinner.setVisibility(View.VISIBLE);
                       sku_options_layout.findViewById(R.id.quantityAttributeText).setVisibility(View.VISIBLE);
                       selectedOption = "ApplyAttribute";
                       if(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabQuantityAttributeIndex() != 0){
                           quantityAttributeSpinner.setSelection(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabQuantityAttributeIndex());
                           addEditText(ConfigAttributeSelectionActivity.attributeModelListSelected.get(ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabQuantityAttributeIndex()-1),false,false,quantityAttributeContainer);
                       }
                   }
                   for (AttributeModelConfig config:ConfigAttributeSelectionActivity.attributeModelListSelected){
                       config.setThirdtabQuantityOption(selectedOption);
                   }
               }
           });
           if(ConfigAttributeSelectionActivity.attributeModelListSelected.size() > 0) {
               if (ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabImageOption().equalsIgnoreCase("Single")) {
                   imageRadioGroup.check(R.id.radioImageSingle);
               }else if (ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabImageOption().equalsIgnoreCase("ApplyAttribute")) {
                   imageRadioGroup.check(R.id.radioImageAttribute);
               }
               if (ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabPriceOption().equalsIgnoreCase("Single")) {
                   priceRadioGroup.check(R.id.radioPriceSingle);
               }else if (ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabPriceOption().equalsIgnoreCase("ApplyAttribute")) {
                   priceRadioGroup.check(R.id.radioPriceAttribute);
               }
               if (ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabQuantityOption().equalsIgnoreCase("Single")) {
                   quantityRadioGroup.check(R.id.radioQuantitySingle);
               }else if (ConfigAttributeSelectionActivity.attributeModelListSelected.get(0).getThirdtabQuantityOption().equalsIgnoreCase("ApplyAttribute")) {
                   quantityRadioGroup.check(R.id.radioQuantityAttribute);
               }
           }
       }else if (stepCount == 3) {
           // Step 4: Summary
           topLabel.setText(R.string.step_4_summary);
           for (AttributeModelConfig config : ConfigAttributeSelectionActivity.attributeModelListFinal) {
               View summaryView = LayoutInflater.from(this).inflate(R.layout.item_config_summary, null);

               TextView attributeField = summaryView.findViewById(R.id.attributeLabel);
               EditText nameTxtField = summaryView.findViewById(R.id.nameTxtField);
               EditText skuField = summaryView.findViewById(R.id.skuTxtField);
               EditText quantityField = summaryView.findViewById(R.id.quantityTxtField);
               EditText priceField = summaryView.findViewById(R.id.priceTxtField);
               ImageView imageView = summaryView.findViewById(R.id.imageView);

               skuField.setText(config.getSkulabel());
               nameTxtField.setText(config.getSkulabel());
               attributeField.setText(config.getTitle());
               quantityField.setText(config.getQuantity());
               priceField.setText(config.getPrice());
               if (config.getImage() != null) {
                   imageView.setImageBitmap(config.getImage());
               }else  if(config.getImageUrl() != null){
                   Glide.with(getApplicationContext())
                           .load(config.getImageUrl())
                           .placeholder(R.drawable.placeholder)
                           .error(R.drawable.placeholder)
                           .override(120, 120)
                           .into(imageView);
               }
               // Make fields non-editable
               nameTxtField.setEnabled(false);
               skuField.setEnabled(false);
               quantityField.setEnabled(false);
               priceField.setEnabled(false);
               groupedProductView.addView(summaryView);
           }
       }
    }

    private void addImageView(AttributeModelConfig attributeModel,boolean isMain,LinearLayout container) {
        container.removeAllViews();
        container.setVisibility(View.VISIBLE);
        if(!isMain) {
            for (AttributeModelConfig attribute : attributeModel.getAttributeArray()) {
                if (attribute.isChecked()) {
                    attribute.setThirdtabImageAttributeIndex(attributeModel.getThirdtabImageAttributeIndex());
                    View layout = View.inflate(this, R.layout.create_product_add_multiple_images, null);
                    ImageView imageView = layout.findViewById(R.id.MultiVendor_productimage);
                    layout.findViewById(R.id.default_checkbox).setVisibility(View.GONE);
                    layout.findViewById(R.id.remove_image).setVisibility(View.GONE);
                    TextView title =  layout.findViewById(R.id.imagetitle);
                    title.setVisibility(View.VISIBLE);
                    title.setText((attribute.getLabel() != null ? attribute.getLabel() : "") + "*");
                    if(attribute.getImage() != null){
                        imageView.setImageBitmap(attribute.getImage());
                    }
                    layout.findViewById(R.id.MultiVendor_clickproductimage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImagePickerManager.presentImagePicker(ConfigAttributeSelectionActivity.this,1, new ImagePickerManager.ImagePickerCallback() {
                                @Override
                                public void onImagePicked(@Nullable Bitmap bitmap) {
                                    if (bitmap != null) {
                                        attribute.setImage(bitmap);
                                        imageView.setImageBitmap(bitmap);  // set in your ImageView
                                    }
                                }
                            });
                        }
                    });
                    layout.findViewById(R.id.MultiVendor_browseproductimage).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImagePickerManager.presentImagePicker(ConfigAttributeSelectionActivity.this,2, new ImagePickerManager.ImagePickerCallback() {
                                @Override
                                public void onImagePicked(@Nullable Bitmap bitmap) {
                                    if (bitmap != null) {
                                        attribute.setImage(bitmap);
                                        imageView.setImageBitmap(bitmap);  // set in your ImageView
                                    }
                                }
                            });
                        }
                    });
                    container.addView(layout);
                }
            }
        }else{
            // Create input field for each attribute
            View layout = View.inflate(this, R.layout.create_product_add_multiple_images, null);
            ImageView imageView = layout.findViewById(R.id.MultiVendor_productimage);
            layout.findViewById(R.id.default_checkbox).setVisibility(View.GONE);
            layout.findViewById(R.id.remove_image).setVisibility(View.GONE);
            layout.findViewById(R.id.imagetitle).setVisibility(View.VISIBLE);
            TextView title =  layout.findViewById(R.id.imagetitle);
            title.setVisibility(View.VISIBLE);
            title.setText(getString(R.string.image) + "*");
            if(attributeModel.getImage() != null){
                imageView.setImageBitmap(attributeModel.getImage());
            }
            layout.findViewById(R.id.MultiVendor_clickproductimage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePickerManager.presentImagePicker(ConfigAttributeSelectionActivity.this,1, new ImagePickerManager.ImagePickerCallback() {
                        @Override
                        public void onImagePicked(@Nullable Bitmap bitmap) {
                            if (bitmap != null) {
                                attributeModel.setImage(bitmap);
                                imageView.setImageBitmap(bitmap);  // set in your ImageView
                            }
                        }
                    });
                }
            });

            layout.findViewById(R.id.MultiVendor_browseproductimage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePickerManager.presentImagePicker(ConfigAttributeSelectionActivity.this,2, new ImagePickerManager.ImagePickerCallback() {
                        @Override
                        public void onImagePicked(@Nullable Bitmap bitmap) {
                            if (bitmap != null) {
                                attributeModel.setImage(bitmap);
                                imageView.setImageBitmap(bitmap);  // set in your ImageView
                            }
                        }
                    });
                }
            });
            container.addView(layout);
        }

    }

    private void addEditText(AttributeModelConfig attributeModel,boolean isPrice,boolean isMain,LinearLayout container) {
        container.removeAllViews();
        container.setVisibility(View.VISIBLE);
        if(!isMain) {
            for (AttributeModelConfig attribute : attributeModel.getAttributeArray()) {
                if (attribute.isChecked()) {
                    // Create input field for each attribute
                    if(isPrice)
                        attribute.setThirdtabPriceAttributeIndex(attributeModel.getThirdtabPriceAttributeIndex());
                    else
                        attribute.setThirdtabQuantityAttributeIndex(attributeModel.getThirdtabQuantityAttributeIndex());
                    LinearLayout inputLayout = new LinearLayout(this);
                    inputLayout.setOrientation(LinearLayout.VERTICAL);
                    TextView inputLabel = new TextView(this);
                    inputLabel.setText((attribute.getLabel() != null ? attribute.getLabel() : "") + "*");
                    inputLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                    EditText inputField = new EditText(this);
                    inputField.setInputType(InputType.TYPE_CLASS_NUMBER |
                            (isPrice ? InputType.TYPE_NUMBER_FLAG_DECIMAL : 0));
                    inputField.setText(isPrice ? attribute.getPrice() : attribute.getQuantity());

                    inputField.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (isPrice) {
                                attribute.setPrice(s.toString());
                            } else {
                                attribute.setQuantity(s.toString());
                            }
                        }
                    });
                    inputLayout.addView(inputLabel);
                    inputLayout.addView(inputField);
                    container.addView(inputLayout);
                }
            }
        }else{
            // Create input field for each attribute
            LinearLayout inputLayout = new LinearLayout(this);
            inputLayout.setOrientation(LinearLayout.VERTICAL);

            TextView inputLabel = new TextView(this);
            inputLabel.setText((isPrice ? getString(R.string.price): getString(R.string.qty)) + "*");
            inputLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            EditText inputField = new EditText(this);
            inputField.setInputType(InputType.TYPE_CLASS_NUMBER |
                    (isPrice ? InputType.TYPE_NUMBER_FLAG_DECIMAL : 0));
            inputField.setText(isPrice ? attributeModel.getPrice() : attributeModel.getQuantity());

            inputField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (isPrice) {
                        attributeModel.setPrice(s.toString());
                    } else {
                        attributeModel.setQuantity(s.toString());
                    }
                }
            });
            inputLayout.addView(inputLabel);
            inputLayout.addView(inputField);
            container.addView(inputLayout);
        }

    }

    private void onBackButtonClicked() {
        if (stepCount > 0) {
            stepCount--;
            updateUI();
        }
    }

    private void onNextButtonClicked() {
        if (stepCount < 3) {
            if (stepCount <= 0) {
                // Filter selected attributes
                ConfigAttributeSelectionActivity.attributeModelListSelected.clear();
                for (AttributeModelConfig config : attributeModelList) {
                    if (config.isChecked()) {
                        ConfigAttributeSelectionActivity.attributeModelListSelected.add(config);
                    }
                }

                if (ConfigAttributeSelectionActivity.attributeModelListSelected.isEmpty()) {
                    showToast(getString(R.string.please_select_attribute_first));
                    return;
                }
            } else if (stepCount == 1) {
                // Validate attribute values are selected
                for (AttributeModelConfig config : ConfigAttributeSelectionActivity.attributeModelListSelected) {
                    boolean hasSelectedValues = false;
                    for (AttributeModelConfig child : config.getAttributeArray()) {
                        if (child.isChecked()) {
                            hasSelectedValues = true;
                            break;
                        }
                    }

                    if (!hasSelectedValues) {
                        showToast("Select options for all attributes or remove unused attributes.");
                        return;
                    }
                }
            } else if (stepCount == 2) {
                // Validate images, prices, quantities
                AttributeModelConfig firstSelected = ConfigAttributeSelectionActivity.attributeModelListSelected.get(0);

                // Image validation
                if ("Single".equals(firstSelected.getThirdtabImageOption())) {
                    if (firstSelected.getImage() == null && firstSelected.getImageUrl() == null) {
                        showToast("Please choose image.");
                        return;
                    }
                } else if ("ApplyAttribute".equals(firstSelected.getThirdtabImageOption())) {
                    if (firstSelected.getThirdtabImageAttributeIndex() == 0) {
                        showToast("Please select attribute for images section.");
                        return;
                    } else {
                        AttributeModelConfig parentConfig = ConfigAttributeSelectionActivity.attributeModelListSelected.get(firstSelected.getThirdtabImageAttributeIndex() - 1);
                        for (AttributeModelConfig child : parentConfig.getAttributeArray()) {
                            if (child.isChecked() && child.getImage() == null && child.getImageUrl() == null) {
                                showToast("Please select images for your attribute.");
                                return;
                            }
                        }
                    }
                }

                // Similar validation for price and quantity...
            }

            stepCount++;
            if (stepCount == 3) {
                generateFinalConfigurations();
            }
            updateUI();
        } else if (stepCount >= 3) {
            // Return results
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void generateFinalConfigurations() {
        ConfigAttributeSelectionActivity.attributeModelListFinal.clear();

        // Generate matrix of selected attributes
        List<List<AttributeModelConfig>> matrix = new ArrayList<>();
        for (AttributeModelConfig config : ConfigAttributeSelectionActivity.attributeModelListSelected) {
            List<AttributeModelConfig> selectedValues = new ArrayList<>();
            for (AttributeModelConfig child : config.getAttributeArray()) {
                if (child.isChecked()) {
                    selectedValues.add(child);
                }
            }
            matrix.add(selectedValues);
        }

        CombinationGenerator combinationGenerator = new  CombinationGenerator();
        // Generate combinations
        List<String> combinationsAttributeWithValue = combinationGenerator.generateAttributeWithValueCombinations(matrix);
        List<String> combinationsAttribute = combinationGenerator.generateCombinations(matrix);
        List<String> combinationsPrice = combinationGenerator.generatePriceCombinations(matrix, ConfigAttributeSelectionActivity.attributeModelListSelected.get(0));
        List<String> combinationsQuantity = combinationGenerator.generateQuantityCombinations(matrix, ConfigAttributeSelectionActivity.attributeModelListSelected.get(0));
        List<Bitmap> combinationsImage = combinationGenerator.generateImageCombinations(matrix, ConfigAttributeSelectionActivity.attributeModelListSelected.get(0));

        // Process each combination
        for (int index = 0; index < combinationsAttribute.size(); index++) {
            AttributeModelConfig finalModel = new AttributeModelConfig();

            // Set attribute with value
            finalModel.setAttributeWithValue(combinationsAttributeWithValue.get(index));

            // Process title and SKU label
            String title = combinationsAttribute.get(index);
            StringBuilder skuLabel = new StringBuilder();
            String[] arr = title.split("::! ");

            for (String part : arr) {
                String[] subArr = part.split(":;");
                if (subArr.length > 1) {
                    if (skuLabel.length() == 0) {
                        skuLabel.append(subArr[1].replace(" ", "-"));
                    } else {
                        skuLabel.append("-").append(subArr[1].replace(" ", "-"));
                    }
                }
            }

            finalModel.setNamelabel(skuLabel.toString());
            finalModel.setSkulabel(skuLabel.toString());
            // Format title
            title = title.replace("::! ", " ").replace(":;", ":");
            finalModel.setTitle(title);

            // Set price if available
            if (index < combinationsPrice.size()) {
                finalModel.setPrice(combinationsPrice.get(index));
            }

            // Set quantity if available
            if (index < combinationsQuantity.size()) {
                finalModel.setQuantity(combinationsQuantity.get(index));
            }

            // Set image if available
            if (index < combinationsImage.size()) {
                finalModel.setImage(combinationsImage.get(index));
            }

            // Process attribute IDs
            String attributeWithValue = finalModel.getAttributeWithValue() != null ?
                    finalModel.getAttributeWithValue() : "";
            String[] attributeValueArr = attributeWithValue.split("::!");
            List<String> attributeIdsArr = new ArrayList<>();

            for (String attribute : attributeValueArr) {
                String[] subArr = attribute.split(":;");
                if (subArr.length > 1) {
                    attributeIdsArr.add(subArr[1]);
                }
            }
            // Check against edit list
            for (AttributeModelConfig editModel : ConfigAttributeSelectionActivity.attributeModelEditListFinal) {
                if (editModel.getAttributeIdsArr() != null &&
                        new HashSet<>(attributeIdsArr).equals(new HashSet<>(editModel.getAttributeIdsArr()))) {
                    finalModel.setSkulabel(editModel.getAssicoiatProductSku());
                    finalModel.setNamelabel(editModel.getNamelabel());
                    finalModel.setAssicoiatProductSku(editModel.getAssicoiatProductSku());
                    finalModel.setAssicoiatProductId(editModel.getAssicoiatProductId());
                    finalModel.setImageUrl(editModel.getImageUrl());

                    if (finalModel.getPrice() == null) {
                        finalModel.setPrice(editModel.getPrice());
                    }

                    if (finalModel.getQuantity() == null) {
                        finalModel.setQuantity(editModel.getQuantity());
                    }

                    if (finalModel.getWeight() == null) {
                        finalModel.setWeight(editModel.getWeight());
                    }
                }
            }

            ConfigAttributeSelectionActivity.attributeModelListFinal.add(finalModel);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showInputDialog(String title, String message, String positiveButton,
                                 String negativeButton, String hint, String text,
                                 final InputDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        final EditText input = new EditText(this);
        input.setHint(hint);
        input.setText(text);
        builder.setView(input);

        builder.setPositiveButton(positiveButton, (dialog, which) -> {
            listener.onInputReceived(input.getText().toString());
        });
        builder.setNegativeButton(negativeButton, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    interface InputDialogListener {
        void onInputReceived(String input);
    }

    public void saveEditAttribute(AttributeModelConfig mainConfig,AttributeModelConfig child, int childIndex, int deleteIndex) {
            try {
                Map<String, Object> optionValue = new HashMap<>();
                Map<String, List<String>> value = new HashMap<>();
                List<String> optionLabelValue = new ArrayList<>();

                optionValue.put("attribute_code", mainConfig.getAttributeCode());

                for (AttributeModelConfig attribute : mainConfig.getAttributeArray()) {
                    String val = attribute.getValue() != null ? attribute.getValue() : "";
                    String label = attribute.getLabel() != null ? attribute.getLabel() : "";
                    value.put(val, Collections.singletonList(label));
                    optionLabelValue.add(val);
                }

                if (childIndex == -1) {
                    value.put("option_" + (value.size() + 1), Collections.singletonList(child.getLabel() != null ? child.getLabel() : ""));
                } else if (deleteIndex != -1) {
                    Map<String, String> deleteMap = new HashMap<>();
                    deleteMap.put(child.getValue() != null ? child.getValue() : "", "1");
                    optionValue.put("delete", deleteMap);
                }
                Map<String, Object> optionMap = new HashMap<>();
                optionMap.put("value", value);
                optionValue.put("option", optionMap);
                JSONObject attribute_data = new JSONObject(optionValue);
                LinkedHashMap<String, String> config_param = new LinkedHashMap<>();
                config_param.put("attribute_id", mainConfig.getAttributeId());
                config_param.put("attribute_data", attribute_data.toString());
                config_param.put("vendor_id", session.getVendorid());
                config_param.put("hashkey", session.getHahkey());
                Ced_MultiVendor_ClientRequestResponse crr = new Ced_MultiVendor_ClientRequestResponse(output -> {
                    JSONObject jsonResponse = new JSONObject(output.toString());
                    JSONObject data = jsonResponse.optJSONObject("data");
                    if (data != null) {
                        String success = data.optString("success");
                        String message = data.optString("message");
                        if (childIndex == -1) {
                            JSONArray optionsArr = data.optJSONArray("option");
                            if (optionsArr != null) {
                                for (int i = 0; i < optionsArr.length(); i++) {
                                    JSONObject option = optionsArr.optJSONObject(i);
                                    if (option != null) {
                                        String optionValueStr = option.optString("value");
                                        if (!optionLabelValue.contains(optionValueStr)) {
                                            child.setValue(optionValueStr);
                                            mainConfig.getAttributeArray().add(child);
                                            updateUI();
                                        }
                                    }
                                }
                            }
                        } else if (deleteIndex != 0) {
                            mainConfig.getAttributeArray().remove(deleteIndex);
                            updateUI();
                        }
                    }
                }, ConfigAttributeSelectionActivity.this, "POST", config_param);
                crr.execute(session.getBase_Url() + "vproductattributeapi/index/saveAttribute");
            } catch(Exception e) {
                e.printStackTrace();
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePickerManager.handleActivityResult(requestCode, resultCode, data, this);
    }

}