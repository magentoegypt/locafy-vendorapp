//
//  TreeViewController.swift
//  RATreeViewExamples
//
//  Created by Rafal Augustyniak on 21/11/15.
//  Copyright © 2015 com.Augustyniak. All rights reserved.
//


import UIKit
import RATreeView

class CategoryTreeViewController: ced_VendorBaseClass, RATreeViewDelegate, RATreeViewDataSource {

    var treeView : RATreeView!
    var data : [DataObject] = []
    var categoryJson: JSON!
    var create_prod = false

    
    var categoriesList = [String:[String]]()
    var subcategoriesData = [String:[String]]()
    var selectedFiltersIds = [Int]();
    var buttonsView: SaveContinueButtonView!
    var config = [[String:String]]();
    var product_id = "";
    var selectedProductTypeId = "";
    var selectedAttributeSetId = "";
    var stock = [String:String]();
    var taxes = [String:String]();
    var tabs = [String:String]();
    var storeViews = [String:[String:[String]]]();
    var websites = [String:String]();
    
    var attributes : JSON!;
    var configSelected = [String:String]();
    //
    var proceeds: Bool = false;
    
    var proDetailArray = [[String:String]]();
    
    var baseURL = String();
    var isEditCase = false;
    //edit time
    //var selectedProductTypeId = "";
    var attributeSetId = String();
    var productType = String();
    
    let screenSize: CGRect = UIScreen.main.bounds;
    let sub = CGFloat(20);
    //let colorString = mobi_common.getInfoPlist("theme_color") as! String;
    var selectedCategories = [String]();
    var selectCategoryView: SelectedCategoriesView!
    
    var selectedWebsitesIds = [Int]();//use also at edit time
    var selectedFiltersIdsString = String();
    var selectedWebsitesIdsString = String();
    var stackHeight = CGFloat(0);
    
    convenience init() {
        self.init(nibName : nil, bundle: nil)
    }

    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        //data = TreeViewController.commonInit()
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }

    required init?(coder aDecoder: NSCoder) {
        //data = TreeViewController.commonInit()
        super.init(coder: aDecoder)
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = DynamicColor.ViewBackgroundColor

        print(subcategoriesData)
        title = "Categories"
        showData();
        //setupTreeView()
    }

    
    
    @objc func showData()
    {
        print("-----selected filter ids-----")
        print(selectedFiltersIds)
        var dataSource=[DataObject]()
        let maincatename="default"
        let maincateimage="unchecked"
        let maincateId="10000000"
        var hasChildd="false"
        if let catJson = categoryJson
        {
            hasChildd="true"
        }
        
        var subDataSource=[DataObject]()
        if hasChildd=="true"{
            subDataSource=addDataToDataSource(value: categoryJson, cateName: maincatename, cateId: maincateId, cateImage: maincateimage)
            dataSource=subDataSource
        }
        self.data=dataSource
        setupTreeView()
    }
    
    func addDataToDataSource(value:JSON, cateName: String, cateId: String, cateImage: String)->[DataObject]{
        
        var cellDataSource=[DataObject]();
        for index in value.arrayValue
        {
            
            let mainName = index["main_category"].stringValue.components(separatedBy: "#").filter { !$0.isEmpty }
            let cateName=mainName[1]
            let cateImage="unchecked"
            let cateId=mainName[0]
            var hasChildd="false"
            if(index["sub_categories"].exists())
            {
                hasChildd="true"
            }
            if hasChildd=="true"{
                let dataSource=addDataToDataSource(value: index["sub_categories"], cateName: cateName, cateId: cateId, cateImage: cateImage)
                
                let data=DataObject(name: cateName, children: dataSource ,id: cateId,image:cateImage)
                cellDataSource.append(data)
            }
            else
            {
                let data=DataObject(name: cateName,id: cateId,image:cateImage)
                cellDataSource.append(data)
            }
        }
        return cellDataSource
        
    }
    
    @objc func continueButtonClicked(_ sender: UIButton)
    {
        let viewcontrollor = UIStoryboard.init(name: "ProductAddon", bundle: nil).instantiateViewController(withIdentifier: "website_Category_ViewController") as! Website_Category_ViewController;
        
        //edit time
        viewcontrollor.isEditCase = isEditCase;
        viewcontrollor.selectedFiltersIds = selectedFiltersIds;
        viewcontrollor.selectedWebsitesIds = selectedWebsitesIds;
        //edit time
        viewcontrollor.subcategoriesData = subcategoriesData;
        viewcontrollor.config = config;
        viewcontrollor.selectedProductTypeId = selectedProductTypeId;
        viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
        viewcontrollor.stock = self.stock;
        viewcontrollor.taxes = self.taxes;
        viewcontrollor.tabs = self.tabs;
        viewcontrollor.storeViews = self.storeViews;
        viewcontrollor.websites = self.websites;
        viewcontrollor.categoriesList = self.categoriesList;
        viewcontrollor.attributes = attributes;
        viewcontrollor.product_id = self.product_id;
        viewcontrollor.configSelected = self.configSelected;
        viewcontrollor.create_prod = create_prod
        self.navigationController?.pushViewController(viewcontrollor, animated: true)
        
        
        
        /*if(selectedProductTypeId.lowercased() == "configurable".lowercased())
         {
         
         let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "configAttributeSelectionViewController") as! ConfigAttributeSelectionViewController;
         
         
         viewcontrollor.config = self.config;
         viewcontrollor.selectedProductTypeId = selectedProductTypeId;
         viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
         viewcontrollor.stock = self.stock;
         viewcontrollor.taxes = self.taxes;
         viewcontrollor.tabs = self.tabs;
         viewcontrollor.storeViews = self.storeViews;
         viewcontrollor.websites = self.websites;
         viewcontrollor.categoriesList = self.categoriesList;
         viewcontrollor.attributes = jsonData["data"]["attributes"];
         viewcontrollor.categoryJson = self.categoryJSON
         
         
         self.navigationController?.pushViewController(viewcontrollor, animated: true)
         
         return;
         }*/
        return;
    }
    
    @objc func setupTreeView() -> Void {
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        buttonsView = SaveContinueButtonView(frame: CGRect(x: 0, y:self.view.frame.height - 110, width: self.view.frame.width, height: 70))
        buttonsView.heightAnchor.constraint(equalToConstant: 0)
        view.addSubview(buttonsView);
        
        buttonsView.continueButton.addTarget(self, action: #selector(continueButtonClicked(_:)), for: .touchUpInside);
        buttonsView.continueButton.setTitle("Save".localized, for: UIControl.State());
        buttonsView.continueButton.setThemeColor();
        /*selectCategoryView = SelectedCategoriesView(frame: CGRect(x: 0, y: 110, width: self.view.frame.width, height: 0))
        self.view.addSubview(selectCategoryView);
        selectCategoryView.stackViewHeight.constant = 0;*/
        treeView = RATreeView(frame: CGRect(x: 0, y: 80, width: self.view.frame.width, height: self.view.frame.height - 180))
        treeView.register(UINib(nibName: String(describing: CategoryTreeTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: CategoryTreeTableViewCell.self))
        treeView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        treeView.delegate = self;
        treeView.dataSource = self;
        treeView.treeFooterView = UIView()
        treeView.backgroundColor = .clear
        view.addSubview(treeView)
        view.addSubview(buttonsView);
        if(data.count>0){
            treeView.expandRow(forItem: data[0])
        }
    }

    

    

    //MARK: RATreeView data source

    @objc func treeView(_ treeView: RATreeView, numberOfChildrenOfItem item: Any?) -> Int {
        if let item = item as? DataObject {
            return item.children.count
        } else {
            return self.data.count
        }
    }

    
    
    var checker:DataObject?=nil
    @objc func treeView(_ treeView: RATreeView, child index: Int, ofItem item: Any?) -> Any {
        print("^^^^^^")
        if let item = item as? DataObject {
            return item.children[index]
        } else {
            
            return data[index] as AnyObject
            
        }
    }

    @objc func treeView(_ treeView: RATreeView, cellForItem item: Any?) -> UITableViewCell {
        print("CELLCREATED")
        let cell = treeView.dequeueReusableCell(withIdentifier: String(describing: CategoryTreeTableViewCell.self)) as! CategoryTreeTableViewCell
        let item = item as! DataObject
        cell.tag = Int(item.id)!;
            if let index = selectedFiltersIds.index(of: Int(item.id)!) {
                item.image="checked";
                /*if(!selectedCategories.contains(item.name))
                 {
                 selectedCategories.append(item.name)
                 }*/
                
            }
            else
            {
                item.image="unchecked";
                /*if let categSelected = selectedCategories.index(of: item.name)
                 {
                 self.selectedCategories.remove(at: categSelected);
                 }*/
                
            }
        //reloadCategories();
        print(item)
        if(item.name.lowercased() == "Default Category".lowercased()){
            item.image="unchecked";
        }
        let level = treeView.levelForCell(forItem: item)
        let detailsText = "Number of children \(item.children.count)"
        cell.selectionStyle = .none
        
        cell.setup(withTitle: item.name, detailsText: detailsText, level: level, additionalButtonHidden: false,img: item.image)
        return cell
    }

    @objc func treeView(_ treeView: RATreeView, didSelectRowForItem item: Any) {
       
        if let cell=treeView.cell(forItem: item) as? CategoryTreeTableViewCell{
            let image=cell.rightImage.image
            let checkImage=UIImage(named: "IQButtonBarArrowRight")
            let currentImage = cell.img.image
            let checkboxImage = UIImage(named: "unchecked")
            let tag = cell.tag;
            let item = item as! DataObject
            print("--select tag-- \(item.name)")
            print(tag)
            if(currentImage == checkboxImage)
            {
                cell.img.image = UIImage(named: "checked")
                selectedFiltersIds.append(tag);
                /*if(!selectedCategories.contains(item.name))
                {
                    selectedCategories.append(item.name)
                }*/
            }
            else
            {
                cell.img.image = UIImage(named: "unchecked")
                if let indexToRemove = selectedFiltersIds.index(of: tag) {
                    self.selectedFiltersIds.remove(at: indexToRemove);
                }
                /*if let categSelected = selectedCategories.index(of: item.name)
                {
                    self.selectedCategories.remove(at: categSelected);
                }*/
            }
            //reloadCategories()
            if image==checkImage{
                cell.rightImage.image=UIImage(named: "down-arrow")
            }
            else
            {
                cell.rightImage.image=UIImage(named: "IQButtonBarArrowRight")
                
            }
            if(item.name.lowercased() == "Default Category".lowercased()){
                cell.rightImage.image=nil
                cell.img.image = UIImage(named: "unchecked")
            }
            if let data=item as? DataObject{
                if data.children.count == 0{
                    cell.rightImage.image=nil
                }
            }
            cell.rightImage.tintColor=UIColor.black
        }
    }
    
    
    @objc func reloadCategories()
    {
        let catStackView = selectCategoryView.mainStackView;
        for stack in (catStackView?.arrangedSubviews)!
        {
            stack.removeFromSuperview()
            catStackView?.removeArrangedSubview(stack);
            
        }
        stackHeight = CGFloat(0);
        selectCategoryView.stackViewHeight.constant = 0;
        for index in selectedCategories
        {
            let label = UILabel(frame: CGRect(x: 0, y: 0, width: (catStackView?.frame.width)!-10, height: 40));
            catStackView?.addArrangedSubview(label);
            label.text = index;
            stackHeight += 50;
            selectCategoryView.stackViewHeight.constant += 40;
        }
        selectCategoryView.frame = CGRect(x: 0, y: 110, width: self.view.frame.width, height: stackHeight)
        treeView.frame=CGRect(x: 0, y: stackHeight+110, width: self.view.frame.width, height: self.view.frame.height-(stackHeight+110))
    }
    
}


private extension TreeViewController {

    static func commonInit() -> [DataObject] {
        return []
    }

}

