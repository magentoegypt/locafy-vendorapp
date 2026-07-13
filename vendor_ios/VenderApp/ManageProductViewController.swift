/**
* CedCommerce
*
* NOTICE OF LICENSE
*
* This source file is subject to the End User License Agreement (EULA)
* that is bundled with this package in the file LICENSE.txt.
* It is also available through the world-wide-web at this URL:
* http://cedcommerce.com/license-agreement.txt
*
* @category  Ced
* @package   MageNative MultiVendor
* @author    CedCommerce Core Team <connect@cedcommerce.com >
* @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
* @license      http://cedcommerce.com/license-agreement.txt
*/

import UIKit

var store_id:String = "0"
class ManageProductViewController: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate,UIGestureRecognizerDelegate, UITextViewDelegate {
    var categoryJSON: JSON!
    var isAuction=false
    var loading = true;
    @IBOutlet weak var manageProductTableView: UITableView!
     let screenSize: CGRect = UIScreen.main.bounds;
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var addNewProductButton: UIButton!
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var updateButton: UIButton!
    @IBOutlet weak var allStorebtn: UIButton!
    
    @IBOutlet weak var statusButton: UIButton!
    
    let refreshControl = UIRefreshControl()
    var page:Int = 1
    var productCounter:Int = 0
    var baseURL = String();
    var productId = String();
    //filtersection
    var value11 = ""
    var value12  = ""
    var value21 = ""
    var value22 = ""
    var value31  = ""
    var value32 = ""
    var value4 = ""
    var value5  = ""
    var value6 = ""
    var value7 = ""
    var filter = String();
    var productStatus = [String:String]();
    var deliveryStatusArray = ["Enable":"1","Disable":"0"];
    var deliveryProductIdsArray = [String]()
    var productType = [String:String]();
    var deliveryPostString = ""
    
    //
    var subcategories = [String:[String]]();
    var pid: String!
    let heightToUse = CGFloat(340);
    
    var products = [[String:String]]();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        manageProductTableView.refreshControl = refreshControl
        // Do any additional setup after loading the view.
        self.view.backgroundColor = DynamicColor.ViewBackgroundColor
        self.manageProductTableView.backgroundColor = UIColor.groupTableViewBackground
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        ced_navigationBarController().addNavButton(self,str:"no")
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        addNewProductButton.backgroundColor = color
        topWrapperView.makeCard(topWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        if isAuction{
            ///addNewProductButton.isHidden=true
            addNewProductButton.setTitle("Vendor Product Listing".localized, for: UIControl.State());
        }
        else
        {
            //addNewProductButton.isHidden=false
            addNewProductButton.titleLabel?.font = UIFont.boldSystemFont(ofSize: 16.0)
            addNewProductButton.setTitle("Add New Product".localized, for: UIControl.State());
            addNewProductButton.addTarget(self, action: #selector(ManageProductViewController.addNewProductButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        }
        
        
        filterButton.setTitle("Continue".localized, for: UIControl.State());
        filterButton.addTarget(self, action: #selector(ManageProductViewController.filterButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        //self.statusButton.addTarget(self, action: #selector(statusButtonClicked(_:)), for: .touchUpInside);
        //self.updateButton.addTarget(self, action: #selector(updateDeliveryButtonClicked(_:)), for: .touchUpInside);
        self.manageProductTableView.delegate = self;
        self.manageProductTableView.dataSource = self;
        self.fetchProductListing(deliveryString: "");
        if(store_id != "0"){
            for buttons in Ced_CommonVendor.storeList {
                if(buttons.store_id == store_id){
                    self.allStorebtn.setTitle(buttons.name, for: .normal)
                }
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
       // Code to refresh table view
        refreshData()
    }
    
    func refreshData(){
        self.page = 1;
        products = [[String:String]]()
        manageProductTableView.reloadData()
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.fetchProductListing(deliveryString: "");
    }
    
    /*@objc func updateDeliveryButtonClicked(_ sender: UIButton)
    {
        print("----deliveryids---\(deliveryProductIdsArray)")
        if(deliveryProductIdsArray.count>0)
        {
            
            var post = "ids/"
            var deliveryIdString = "";
            for index in deliveryProductIdsArray
            {
                deliveryIdString += index + ","
            }
            if(deliveryIdString != "")
            {
                let lastChar = deliveryIdString[deliveryIdString.index(before: deliveryIdString.endIndex)]
                if(lastChar == ",")
                {
                    let valst = String(deliveryIdString.dropLast())
                    deliveryIdString = valst;
                }
                //deliveryIdString = "{\(tempStr)}"
            }
            post += deliveryIdString
            post += "/status/\(deliveryStatusArray[statusButton.currentTitle!]!)"
            self.deliveryPostString = post
            products = [[String:String]]();
            fetchProductListing(deliveryString: post)
        }
        
    }
    
    @objc func statusButtonClicked(_ sender: UIButton)
    {
        let dropDown = DropDown();
        var dropDownToShow = Array(self.deliveryStatusArray.keys);
        dropDownToShow = dropDownToShow.sorted();
        dropDown.dataSource = dropDownToShow;
        dropDown.selectionAction = {(index, item) in
            
            sender.setTitle(item, for: UIControlState());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            _ = dropDown.show();
        } else {
            _ = dropDown.hide();
        }
    }*/
    
    // MARK: - Table view data source
    
    @objc func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return self.products.count;
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = manageProductTableView.dequeueReusableCell(withIdentifier: "editProductSingleCell", for: indexPath) as! EditProductSingleCell;
        
        cell.label1.text = "Product ID".localized;
        cell.label1.font = UIFont.systemFont(ofSize: 15.0)
        cell.value1.font = UIFont.systemFont(ofSize: 15.0)
        cell.value1.text = products[indexPath.row]["product_id"];
        
        cell.label2.text = "Product Qty".localized;
        cell.label2.font = UIFont.systemFont(ofSize: 15.0)
        cell.value2.font = UIFont.systemFont(ofSize: 15.0)
        cell.value2.text = products[indexPath.row]["qty"];
        if isAuction{
            cell.label5.text = "Product Price".localized;
            cell.value5.text = products[indexPath.row]["price"];
            cell.label5.font = UIFont.systemFont(ofSize: 15.0)
            cell.value5.font = UIFont.systemFont(ofSize: 15.0)
            cell.imageViewHeight.constant=0
            cell.label6.text = "Product Name".localized;
            cell.value6.text = products[indexPath.row]["name"];
            cell.label6.font = UIFont.systemFont(ofSize: 15.0)
            cell.value6.font = UIFont.systemFont(ofSize: 15.0)
            cell.label3.text = "Vendor SKU".localized;
            cell.value3.text = "-";
            cell.label3.font = UIFont.systemFont(ofSize: 15.0)
            cell.value3.font = UIFont.systemFont(ofSize: 15.0)
            cell.label7.text = "Product Status".localized;
            cell.value7.text = "-";
            cell.label7.font = UIFont.systemFont(ofSize: 15.0)
            cell.value7.font = UIFont.systemFont(ofSize: 15.0)
            
        }
        else
        {
            cell.imageViewHeight.constant=128
            cell.label5.text = "Product Price".localized;
            cell.value5.text = products[indexPath.row]["vendor_price"]
            cell.label5.font = UIFont.systemFont(ofSize: 15.0)
            cell.value5.font = UIFont.systemFont(ofSize: 15.0)
            cell.label6.text = "Product Name".localized;
            cell.value6.text = products[indexPath.row]["product_name"]
            cell.label6.font = UIFont.systemFont(ofSize: 15.0)
            cell.value6.font = UIFont.systemFont(ofSize: 15.0)
            cell.label3.text = "Vendor SKU".localized;
            cell.value3.text = products[indexPath.row]["sku"]
            cell.label3.font = UIFont.systemFont(ofSize: 15.0)
            cell.value3.font = UIFont.systemFont(ofSize: 15.0)
            cell.label7.text = "Product Status".localized;
            cell.value7.text = products[indexPath.row]["status"]
            cell.label7.font = UIFont.systemFont(ofSize: 15.0)
            cell.value7.font = UIFont.systemFont(ofSize: 15.0)
        }
        
        
        cell.label4.text = "Product Type".localized;
        cell.value4.text = products[indexPath.row]["type"];
        cell.label4.font = UIFont.systemFont(ofSize: 15.0)
        cell.value4.font = UIFont.systemFont(ofSize: 15.0)
        cell.checkedButton.addTarget(self, action: #selector(itemShippingClicked(_:)), for: .touchUpInside);
        
        
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        if isAuction{
            cell.editButton.isHidden=true
            cell.deleteButton.isHidden=true
            cell.addAuctionButton.isHidden=false
            
            cell.addAuctionButton.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15.0)
            cell.addAuctionButton.backgroundColor = color;
            cell.addAuctionButton.tag = Int(products[indexPath.row]["product_id"]!)!;
            cell.addAuctionButton.addTarget(self, action: #selector(addAuctionButtonTapped(_:)), for: .touchUpInside)
        }
        else
        {
            cell.editButton.isHidden=false
            cell.deleteButton.isHidden=false
            cell.addAuctionButton.isHidden=true
            cell.editButton.setTitle("Edit".localized, for: UIControl.State())
            cell.editButton.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15.0)
            cell.editButton.addTarget(self, action: #selector(ManageProductViewController.editProductRequest(_:)), for: UIControl.Event.touchUpInside)
            if let product_id = products[indexPath.row]["product_id"]{
                if(deliveryProductIdsArray.contains(product_id))
                {
                    cell.checkedButton.setImage(UIImage(named: "checked_checkbox.png"), for: .normal);
                }
                else
                {
                    cell.checkedButton.setImage(UIImage(named: "unchecked_checkbox.png"), for: .normal);
                }
                if let id = Int(product_id){
                    cell.editButton.tag = id
                    cell.checkedButton.tag = id
                }
            }
            
            
            cell.editButton.backgroundColor = color
            cell.deleteButton.setTitle("Delete".localized, for: UIControl.State())
            cell.deleteButton.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15.0)
            cell.deleteButton.addTarget(self, action: #selector(ManageProductViewController.deleteProductRequestConfirmation(_:)), for: UIControl.Event.touchUpInside)
            cell.deleteButton.tag = Int(products[indexPath.row]["product_id"]!)!
            let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
            cell.deleteButton.backgroundColor = colorRed
            if let imgUrl = products[indexPath.row]["product_image"]?.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed){
                cell.productImage.sd_setImage(with: URL(string: imgUrl ), placeholderImage: UIImage(named: "placeholder"))
            }
            
            
            cell.productImage.isUserInteractionEnabled = true
            let tapRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.imageTapped(_:)))
            cell.productImage.addGestureRecognizer(tapRecognizer);
            /*let imgRequest = NSMutableURLRequest(url: URL(string: products[indexPath.row]["product_image"]!)!);
            
            let sessionConfig = URLSessionConfiguration.default
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: imgRequest as URLRequest) { (data, response, error) in
                guard error == nil && data != nil else
                {
                    print("error=\(error)")
                    return;
                }
                
                // check for http errors
                if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
                {
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    print("response = \(response)")
                    return;
                }
                
                // Convert the downloaded data in to a UIImage object
                
                if let data = data{
                    let image = UIImage(data: data)
                    // Store the image in to our cache
                    //self.imageCache[urlString] = image
                    // Update the cell
                    DispatchQueue.main.async
                        {
                            
                            cell.productImage.image = image;
                            cell.productImage.isUserInteractionEnabled = true;
                            
                            let tapRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.imageTapped(_:)))
                            cell.productImage.addGestureRecognizer(tapRecognizer);
                    }
                }
                
                
            }
            
            
            task.resume();*/
        }
        
        
        
        
        cell.backgroundColor = UIColor.init(hexString: "#F2F2F2")
        
        return cell;
    }
    
    func showStores(){
        let actionsheet = UIAlertController(title: "Select Store".localized, message: nil, preferredStyle: .actionSheet)
        print("%^%^%^*(**^&%&%*)_*^&^")
        actionsheet.addAction(UIAlertAction(title: "All Store", style: UIAlertAction.Style.default, handler: {
            action -> Void in
            self.allStorebtn.setTitle("All Store", for: .normal)
            store_id = "0"
            self.refreshData()
        }))
        for buttons in Ced_CommonVendor.storeList {
            if(buttons.code == "eg_ar" || buttons.code == "eg_en"){
                actionsheet.addAction(UIAlertAction(title: buttons.name, style: UIAlertAction.Style.default,handler: {
                    action -> Void in
                    self.allStorebtn.setTitle(buttons.name, for: .normal)
                    store_id = buttons.store_id
                    self.refreshData()
                }))
            }
        }
        actionsheet.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.cancel, handler: {
            action -> Void in
            
        }))
        if(UIDevice().model.lowercased() == "ipad".lowercased()){
            if let popoverController = actionsheet.popoverPresentationController {
                popoverController.sourceView = self.view //to set the source of your alert
                popoverController.sourceRect = CGRect(x: self.view.bounds.midX, y: self.view.bounds.midY, width: 0, height: 0) // you can set this as per your requirement.
                popoverController.permittedArrowDirections = [] //to hide the arrow of any particular direction
            }
        }
        self.present(actionsheet, animated: true, completion: nil)
    }
    
    @IBAction func allStoreButtonClicked(_ sender:UIButton){
        showStores()
    }
    
    @objc func itemShippingClicked(_ sender: UIButton)
    {
        if(sender.currentImage == UIImage(named: "unchecked_checkbox.png"))
        {
            sender.setImage(UIImage(named: "checked_checkbox.png"), for: .normal);
            deliveryProductIdsArray.append(String(sender.tag));
        }
        else
        {
            if(deliveryProductIdsArray.contains("\(sender.tag)"))
            {
                if let index = deliveryProductIdsArray.lastIndex(of: "\(sender.tag)")
                {
                    deliveryProductIdsArray.remove(at: index);
                }
            }
            sender.setImage(UIImage(named: "unchecked_checkbox.png"), for: .normal)
        }
    }
    
    @objc func addAuctionButtonTapped(_ sender: UIButton){
        var proName=""
        
        for item in products{
            if item["product_id"]==String(sender.tag){
                proName=item["name"]!
            }
        }
        let viewControl=UIStoryboard(name: "auctionStoryboard", bundle: nil).instantiateViewController(withIdentifier: "addAuctionViewController") as! addAuctionViewController
        viewControl.proId=String(sender.tag)
        viewControl.proName=proName
        self.navigationController?.pushViewController(viewControl, animated: true)
    }
    @objc func imageTapped(_ gestureRecognizer: UITapGestureRecognizer)
    {
        /* background view */
        let bgCView = UIView();
        bgCView.tag=121;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        self.addgesturesTohideView(self.view);
        
        let imgPopupView = UIImageView();
        imgPopupView.tag = 131;
        imgPopupView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        imgPopupView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width/2,height: screenSize.height/2);
        imgPopupView.center = CGPoint(x: bgCView.frame.size.width  / 2,
            y: bgCView.frame.size.height / 2);
        let tappedImageView = gestureRecognizer.view as! UIImageView;
        imgPopupView.image = tappedImageView.image;
        imgPopupView.contentMode = .scaleAspectFit
        bgCView.addSubview(imgPopupView);
        self.view.addSubview(bgCView);
    }
   
    

    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        if isAuction{
            return heightToUse-128
        }
        return heightToUse;
    }
    
    
    var auctionFilter=auctionProductFilterView()
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        print("filterButtonPressed");
        /* background view */
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        if isAuction{
            //auctionFilter=auctionProductFilterView()
            auctionFilter.frame=CGRect(x: 0, y: 0, width: self.view.frame.width-30, height: 270)
            auctionFilter.center=bgCView.center
            self.addgesturesTohideView(self.view);
            auctionFilter.tag=181
           
            auctionFilter.productId.text=""
            auctionFilter.productName.text=""
            auctionFilter.productType.text=""
            auctionFilter.productPrice.text=""
            auctionFilter.productQty.text=""
            auctionFilter.productType.addTarget(self, action:#selector(isValidNme(_:)), for: .editingDidEnd);
            auctionFilter.productName.addTarget(self, action:#selector(isValidNme(_:)), for: .editingDidEnd);
            auctionFilter.productId.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            auctionFilter.productPrice.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            auctionFilter.productQty.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            auctionFilter.applyButton.addTarget(self, action: #selector(applyButtonPressed(_:)), for: .touchUpInside)
            auctionFilter.clearButton.addTarget(self, action: #selector(clearButtonPressed(_:)), for: .touchUpInside)
            bgCView.addSubview(auctionFilter)
        }
        else
        {
            let longFilterPopupView = LongFilterPopupView();
            self.addgesturesTohideView(self.view);
            longFilterPopupView.tag = 181;
            longFilterPopupView.backgroundColor = UIColor.black;
            longFilterPopupView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 270);
            longFilterPopupView.center.x = self.view.center.x;
            longFilterPopupView.center.y = self.view.center.y+20;
            //coloring
           // longFilterPopupView.topLabel.backgroundColor = color;
            longFilterPopupView.leftButton.backgroundColor = color;
            longFilterPopupView.rightButton.backgroundColor = color;
            longFilterPopupView.topLabel.text = "Vendor App".localized
            longFilterPopupView.label1.text = "Product Price".localized;
            longFilterPopupView.label2.text = "Product Qty".localized;
            longFilterPopupView.label3.text = "Product ID".localized;
            longFilterPopupView.label4.text = "Product Name".localized;
            longFilterPopupView.label5.isHidden = true;
            //longFilterPopupView.label5.text = "Product SKU";
            longFilterPopupView.label6.text = "Product Status".localized;
            longFilterPopupView.value6.setTitle(value6, for: UIControl.State());
            longFilterPopupView.value6.addTarget(self, action: #selector(ManageProductViewController.showProductStatusDropdown(_:)), for: UIControl.Event.touchUpInside);
            longFilterPopupView.label7.text = "Product Type".localized;
            longFilterPopupView.value7.setTitle(value7, for: UIControl.State());
            longFilterPopupView.value7.addTarget(self, action: #selector(ManageProductViewController.showProductTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
            longFilterPopupView.value6.layer.borderColor = UIColor(red: 0.9, green: 0.9, blue: 0.9, alpha: 1.0).cgColor
            longFilterPopupView.value6.layer.borderWidth = 1.0
            longFilterPopupView.value6.layer.cornerRadius = 5
            longFilterPopupView.value7.layer.borderColor = UIColor(red: 0.9, green: 0.9, blue: 0.9, alpha: 1.0).cgColor
            longFilterPopupView.value7.layer.borderWidth = 1.0
            longFilterPopupView.value7.layer.cornerRadius = 5
            longFilterPopupView.value11.delegate = self
            longFilterPopupView.value12.delegate = self
            longFilterPopupView.value21.delegate = self
            longFilterPopupView.value22.delegate = self
            longFilterPopupView.value31.delegate = self
            longFilterPopupView.value32.delegate = self
            longFilterPopupView.value11.text = value11;
            longFilterPopupView.value12.text = value12;
            longFilterPopupView.value21.text = value21;
            longFilterPopupView.value22.text = value22;
            longFilterPopupView.value31.text = value31;
            longFilterPopupView.value32.text = value32;
            longFilterPopupView.value4.text = value4;
            longFilterPopupView.value5.text = value5;
            longFilterPopupView.value5.isHidden = true;
            longFilterPopupView.value5LabelHeight.constant = 0;
            longFilterPopupView.value5TopHeight.constant = 0;
            PopupLookImprovement.designTextView(longFilterPopupView.value11);
            PopupLookImprovement.designTextView(longFilterPopupView.value12);
            PopupLookImprovement.designTextView(longFilterPopupView.value21);
            PopupLookImprovement.designTextView(longFilterPopupView.value22);
            PopupLookImprovement.designTextView(longFilterPopupView.value31);
            PopupLookImprovement.designTextView(longFilterPopupView.value32);
            PopupLookImprovement.designTextView(longFilterPopupView.value4);
            //PopupLookImprovement.designTextView(longFilterPopupView.value5);
            longFilterPopupView.leftButton.setTitle("Filter".localized, for: UIControl.State());
            longFilterPopupView.leftButton.addTarget(self, action: #selector(ManageProductViewController.applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
            longFilterPopupView.rightButton.setTitle("Reset".localized, for: UIControl.State());
            longFilterPopupView.rightButton.addTarget(self, action: #selector(ManageProductViewController.resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
            bgCView.addSubview(longFilterPopupView)
        }
        self.view.addSubview(bgCView);
    }
    
    @objc func applyButtonPressed(_ sender: UIButton){
        filter = "";
        filter += "{";
        filter += "\""+"product_id"+"\":\""+auctionFilter.productId.text!+"\",";
        filter += "\""+"name"+"\":\""+auctionFilter.productName.text!+"\",";
        filter += "\""+"type"+"\":\""+auctionFilter.productType.text!+"\",";
        filter += "\""+"price"+"\":\""+auctionFilter.productPrice.text!+"\",";
        filter += "\""+"qty"+"\":\""+auctionFilter.productQty.text!+"\"}";
        print(filter);
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.page = 1;
        products = [[String:String]]();
        self.fetchProductListing(deliveryString: "");
    }
    
    @objc func clearButtonPressed(_ sender: UIButton){
        
        auctionFilter.id.text=""
        auctionFilter.productId.text=""
        auctionFilter.productName.text=""
        auctionFilter.productType.text=""
        auctionFilter.productPrice.text=""
        auctionFilter.productQty.text=""
        filter = String();
        self.page = 1;
        products = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.fetchProductListing(deliveryString: "");
    }
    @objc func showProductStatusDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = Array(productStatus.keys);
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            _ = dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func showProductTypeDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = Array(self.productType.keys);
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            _ = dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func textViewDidEndEditing(_ textView: UITextView) {
        
        if !self.isValidNumber(Number: textView.text!)
        {
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information".localized, showMsg: "Use Numbers".localized);
            textView.text = ""
            return
        }
    }
    
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ManageProductViewController.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ManageProductViewController.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ManageProductViewController.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ManageProductViewController.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ManageProductViewController.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview();
        
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if touch.view!.isDescendant(of: auctionFilter){
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? ced_orderFilterpopup {
            if(touch.view!.isDescendant(of: innerView) || touch.view!.isDescendant(of: manageProductTableView) )
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    @objc func applyFilterTapped(_ sender:UIButton)
    {
        print("applyFilterTapped");
        
        if let view = self.view.viewWithTag(181) as? LongFilterPopupView
        {
            if(view.value11.text != nil){
                value11 =  view.value11.text!
            }
            if(view.value12.text != nil){
                value12 = view.value12.text!
            }
            if(view.value21.text != nil){
                value21 = view.value21.text!
            }
            if(view.value22.text != nil){
                value22 = view.value22.text!
            }
            if(view.value31.text != nil){
                value31 = view.value31.text!
            }
            if(view.value32.text != nil){
                value32 = view.value32.text!
            }
            if(view.value4.text != nil){
                value4 = view.value4.text!
            }
            if(view.value5.text != nil){
                value5 = view.value5.text!
            }
            if(view.value6.titleLabel!.text != nil){
                value6 = view.value6.titleLabel!.text!
            }
            if(view.value7.titleLabel!.text != nil){
                value7 = view.value7.titleLabel!.text!
            }
            
            filter = "";
            filter += "{";
            filter += "\""+"price"+"\":{\"from\":\""+value11+"\",\"to\":\""+value12+"\"},";
            filter += "\""+"qty"+"\":{\"from\":\""+value21+"\",\"to\":\""+value22+"\"},";
            filter += "\""+"entity_id"+"\":{\"from\":\""+value31+"\",\"to\":\""+value32+"\"},";
            filter += "\""+"name"+"\":\""+value4+"\",";
            //filter += "\""+"sku"+"\":\""+value5+"\",";
            if(value6 != "")
            {
                filter += "\""+"check_status"+"\":\""+productStatus[value6]!+"\",";
            }
            else
            {
                filter += "\""+"check_status"+"\":\""+""+"\",";
            }
            
            if(value7 != "")
            {
                filter += "\""+"type_id"+"\":\""+productType[value7]!+"\"}";
            }
            else
            {
                filter += "\""+"type_id"+"\":\""+""+"\"}";
            }
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            products = [[String:String]]();
            self.fetchProductListing(deliveryString: "");
            
        }
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        value11 = ""
        value12  = ""
        value21 = ""
        value22 = ""
        value31  = ""
        value32 = ""
        value4 = ""
        value5  = ""
        value6 = ""
        value7 = ""
        filter = String();
        self.page = 1;
        products = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.fetchProductListing(deliveryString: "");
    }
    
    
    
    @objc func addNewProductButtonPressed(_ sender:UIButton)
    {
        let storyBoard = UIStoryboard(name: "ProductAddon", bundle: nil)
        let Viewcontrol = storyBoard.instantiateViewController(withIdentifier: "addProductAddonFirstPage") as? AddProductAddonFirstPage
        self.navigationController?.pushViewController(Viewcontrol!, animated: true)      
    }
    
    @objc func editProductRequest(_ sender:UIButton)
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        productId = String(sender.tag);
        baseURL = "vproductapi/vproducts/info/";
        
        print(baseURL);
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+productId;
        postString += "&store_id="+store_id;
        print(postString);
        sendRequest(url: baseURL, parameters: postString);
    }
    
    func loadProductInfo(_ productId:String, _ viewcontrollor:ProductDetailsViewController){
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+productId;
        postString += "&store_id="+store_id;
        let request = NSMutableURLRequest(url: URL(string: "\(settings.baseUrl)vproductapi/vproducts/info")!);
        request.httpMethod = "POST";
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "User-Agent")
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) in
            guard error == nil && data != nil else
            {
                print("error=\(error)");
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self);
                    let title = "Error".localized;
                    let message = "Some Network Error Occured!".localized;
                        Alert_File.showNetworkIssue(self,title:title,message:message);
                }
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)");
                print("response = \(postString)");
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self);
                        
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                return;
            }
            // code to fetch values from response :: start
            guard let jsonData = try? JSON(data: data!) else {return}
            DispatchQueue.main.async
                {
                    print("jsonData \(jsonData)");
                    self.productCounter -= 1
                    if(jsonData["data"]["success"].stringValue == "true")
                    {
                        let json = jsonData["data"]["productdata"]
                        viewcontrollor.productJsonArr.append(json)
                    }
                    if(self.productCounter <= 0){
                        Alert_File.removeLoadingIndicator(self);
                        self.navigationController?.pushViewController(viewcontrollor, animated: true)
                    }
            }
        }
        task.resume();
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            guard var jsonData = try? JSON(data: data) else {return}
            if(requestUrl == "vproductapi/vproducts/info/")
            {
                Alert_File.removeLoadingIndicator(self);
                
                print("COOOOOOLLLLLLLL");
                print(jsonData);
                if(jsonData["data"]["success"].stringValue == "true")
                {
                    var stock = [String:String]();
                    var taxes = [String:String]();
                    var tabs = [String:String]();
                    var storeViews = [String:[String:[String]]]();
                    var websites = [String:String]();
                    var categoriesList = [String:[String]]();
                    
                    /** fetching stock values **/
                    /*for (key,val) in jsonData["data"]["stock"]
                    {
                        stock[val.stringValue] = key;
                    }
                    
                    /** fetching stock values **/
                    for (key,val) in jsonData["data"]["taxes"]
                    {
                        taxes[val.stringValue] = key;
                    }*/
                    
                    categoriesList = self.fectchParentAndChildCategories(jsonData["data"]["category"]);
                    
                    
                    
                    
                    /** fetching tabs **/
                    for (key,val) in jsonData["data"]["tabs"]
                    {
                        if(key == "category")
                        {
                            self.categoryJSON = val
                            categoriesList = self.fectchParentAndChildCategories(val);
                        }
                        else if(key == "stores")
                        {
                            
                        }
                        else if(key == "storeViews")
                        {
                            /** fetching websites data **/
                            for (key1,value) in val
                            {
                                var temp = [String:[String]]();
                                for (inrKey,inrVal) in value
                                {
                                    temp[inrKey] = inrVal.arrayObject as? [String];
                                }
                                
                                storeViews[key1] = temp;
                                print("---\(storeViews[key1])")
                            }
                        }
                        else if(key == "websites"){
                            /** fetching websites **/
                            for (key1,val1) in val
                            {
                                websites[val1.stringValue] = key1;
                            }
                        }
                        else
                        {
                            tabs[key] = val.stringValue;
                        }
                        
                    }
                    var configSelected = [String:String]();
                    
                    print("CHECKKKK");
                    if(jsonData["data"]["config_selected_attr"] != nil)
                    {
                        for (k,v) in jsonData["data"]["config_selected_attr"]
                        {
                            //print(k);
                            //print(v);
                            
                            configSelected[v.stringValue] = k;
                        }
                    }
                    //print(configSelected);
                    
                    
                    print("product data----\(jsonData["data"]["productdata"])");
                    
                    var productBasicData = [String:String]();
                    productBasicData["product_brand"] = jsonData["data"]["productdata"]["product_brand"].stringValue
                    productBasicData["entity_id"] = jsonData["data"]["productdata"]["entity_id"].stringValue;
                    productBasicData["entity_type_id"] = jsonData["data"]["productdata"]["entity_type_id"].stringValue;
                    productBasicData["attribute_set_id"] = jsonData["data"]["productdata"]["attribute_set_id"].stringValue;
                    productBasicData["type_id"] = jsonData["data"]["productdata"]["type_id"].stringValue;
                    productBasicData["sku"] = jsonData["data"]["productdata"]["sku"].stringValue;
                    productBasicData["name"] = jsonData["data"]["productdata"]["name"].stringValue;
                    productBasicData["price"] = jsonData["data"]["productdata"]["price"].stringValue;
                    productBasicData["special_price"] = jsonData["data"]["productdata"]["special_price"].stringValue;
                    productBasicData["weight"] = jsonData["data"]["productdata"]["weight"].stringValue;
                    productBasicData["qty"] = jsonData["data"]["productdata"]["stock_item"][0]["qty"].stringValue;
                    productBasicData["is_in_stock"] = jsonData["data"]["productdata"]["is_in_stock"].stringValue;
                    
                    productBasicData["status"] = jsonData["data"]["productdata"]["status"].stringValue;
                    productBasicData["visbility"] = jsonData["data"]["productdata"]["visbility"].stringValue;
                    productBasicData["description"] = jsonData["data"]["productdata"]["description"].stringValue;
                    productBasicData["short_description"] = jsonData["data"]["productdata"]["short_description"].stringValue;
                    productBasicData["tax_class_id"] = jsonData["data"]["productdata"]["tax_class_id"].stringValue;
                    productBasicData["url_key"] = jsonData["data"]["productdata"]["url_key"].stringValue;
                    print("----product data ----- \(productBasicData)")
                    var productSelectedCategories = [Int]();
                    var productSelectedWebsites = [Int]();
                    /*let brandArray = jsonData["data"]["productdata"]["product_brand"].stringValue.components(separatedBy: "\"")
                    var product_brand = [Int]()
                    for item in brandArray{
                        if let item = Int(item){
                            product_brand.append(item)
                        }
                    }*/
                    //selected categories and websites
                    //print("productcategories");
                    for (_,val) in jsonData["data"]["productcategories"]
                    {
                        //print(val);
                        productSelectedCategories.append(Int(val.stringValue)!);
                    }
                    //print("productwebsites");
                    for (_,val) in jsonData["data"]["productwebsites"]
                    {
                        //print(val);
                        productSelectedWebsites.append(Int(val.stringValue)!);
                    }
                    
                    
                    
                    
                    
                    //let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "productDetailsViewController") as! ProductDetailsViewController;
                    let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "productDetailsViewController") as! ProductDetailsViewController;
                    //viewcontrollor.product_brand = product_brand
                    viewcontrollor.isEditCase = true;
                    viewcontrollor.media_image = jsonData["data"]["productdata"]["media_image"];
                    
                    viewcontrollor.productBasicData = productBasicData;
                    viewcontrollor.productSelectedCategories = productSelectedCategories;
                    viewcontrollor.productSelectedWebsites = productSelectedWebsites;
                    
                    viewcontrollor.selectedProductTypeId = jsonData["data"]["productdata"]["type_id"].stringValue;
                    viewcontrollor.selectedAttributeSetId = jsonData["data"]["productdata"]["attribute_set_id"].stringValue;
                    let associated_product_ids = jsonData["data"]["associated_product_ids"].arrayValue
                    let config_selected_attr = jsonData["data"]["config_selected_attr"]
                    productCounter = associated_product_ids.count
                    for index in 0..<associated_product_ids.count{
                        viewcontrollor.associated_product_idsList.append(associated_product_ids[index].stringValue)
                    }
                    config_selected_attr.forEach { (key, json) in
                        viewcontrollor.config_selected_attr.append(json.stringValue)
                    }
                    viewcontrollor.store_id = store_id
                    viewcontrollor.stock = stock;
                    viewcontrollor.subcategoriesData = subcategories;
                    viewcontrollor.taxes = taxes;
                    viewcontrollor.tabs = tabs;
                    viewcontrollor.storeViews = storeViews;
                    viewcontrollor.websites = websites;
                    viewcontrollor.categoriesList = categoriesList;
                    viewcontrollor.attributes = jsonData["data"]["attributes"];
                    viewcontrollor.product_id = productId;
                    viewcontrollor.categoryJson = categoryJSON;
                    viewcontrollor.configSelected = configSelected;
                    //Alert_File.removeLoadingIndicator(self);
                    if(productCounter > 0){
                        viewcontrollor.associated_product_idsList.forEach { id in
                            self.loadProductInfo(id, viewcontrollor)
                        }
                    }else{
                        self.navigationController?.pushViewController(viewcontrollor, animated: true)
                    }
                }
                else
                {
                    self.view.makeToast(jsonData["data"]["message"].stringValue, duration: 2.0, position: .center);
                }
                
            }
            else if(requestUrl == "vendorapi/vproducts/delete/")
            {
                Alert_File.removeLoadingIndicator(self);
                print(jsonData);
                if(jsonData["success"].stringValue != "true")
                {
                    print(jsonData["message"].stringValue);
                    let title = "Error".localized;
                    let message = jsonData["message"].stringValue;
                    Alert_File.showValidationError(self,title:title,message:message);
                    return;
                }
                
                print(jsonData["message"].stringValue);
                self.page = 1;
                self.products = [[String:String]]();
                self.loading = true
                self.manageProductTableView.reloadData()
                self.fetchProductListing(deliveryString: "");
                
            }
            else if(requestUrl == "vendorapi/vproducts/item/\(deliveryPostString)" || requestUrl=="vauctionapi/auction/productlist")
            {
                Alert_File.removeLoadingIndicator(self)
                refreshControl.endRefreshing()
                print(jsonData);
                print(NSString(data: data, encoding: String.Encoding.utf8.rawValue))
                if isAuction{
                    if(jsonData["data"]["success"].stringValue != "true")
                    {
                        self.loading = false;
                        self.manageProductTableView.reloadData();
                        if(page == 1)
                        {
                           // ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: jsonData["data"]["message"].stringValue)
                            self.manageProductTableView.setEmptyMessage(jsonData["data"]["message"].stringValue)
                        }
                        
                        return;
                    }
                    
                    /** fetching product types **/
                    /** fetching product types **/
                    for (_,val) in jsonData["data"]["product_list"]
                    {
                        var tempArray = [String:String]();
                        for(k,v) in val
                        {
                            tempArray[k] = v.stringValue;
                        }
                        self.products.append(tempArray);
                    }
                }
                else
                {
                    for(key,value) in jsonData["data"]["status_array"]
                    {
                        self.productStatus[value.stringValue] = key;
                    }
                    for index in jsonData["data"]["type"].arrayValue
                    {
                        self.productType[index["value"].stringValue] = index["key"].stringValue;
                    }
                    if(jsonData["data"]["success"].stringValue != "true")
                    {
                        self.loading = false;
                        if(page == 1)
                        {
                          //  ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: jsonData["data"]["message"].stringValue)
                            self.manageProductTableView.setEmptyMessage(jsonData["data"]["message"].stringValue)
                        }
                        self.manageProductTableView.reloadData();
                        return;
                    }
                    
                    /** fetching product types **/
                    /** fetching product types **/
                    for (_,val) in jsonData["data"]["products"]
                    {
                        var tempArray = [String:String]();
                        for(k,v) in val
                        {
                            tempArray[k] = v.stringValue;
                        }
                        self.products.append(tempArray);
                    }
                }
                self.manageProductTableView.restore()
                self.manageProductTableView.reloadData();
            }
        }
        
    }
    
    func fectchParentAndChildCategories(_ browseByJSON:JSON)->[String:[String]]
    {
        categoryJSON = browseByJSON;
        print("----\(browseByJSON)")
        //print(browseByJSON);
        var categoriesList = [String:[String]]();
        for val in browseByJSON.arrayValue
        {
            var temp = [String]();
            for inrVal in val["sub_categories"].arrayValue
            {
                print("--sffs---")
                temp.append(inrVal["main_category"].stringValue);
                if(inrVal["sub_categories"].exists())
                {
                    var subArray = [String]()
                    for inVal in inrVal["sub_categories"].arrayValue
                    {
                         subArray.append(inVal["main_category"].stringValue);
                        
                    }
                    let subcat = inrVal["main_category"].stringValue
                    let impArray = subcat.components(separatedBy: "#");
                    subcategories[impArray.first!]=subArray
                }
            }
            categoriesList[val["main_category"].stringValue] = temp;
        }
        return(categoriesList);
    }
    
    @objc func deleteProductRequestConfirmation(_ sender:UIButton)
    {
        let showTitle = "Confirmation".localized;
        let showMsg = "Are You Sure You Want To Delete It?".localized;
        
        if #available(iOS 8.0, *) {
            let confirmationAlert = UIAlertController(title: showTitle, message: showMsg, preferredStyle: UIAlertController.Style.alert)
            confirmationAlert.addAction(UIAlertAction(title: "Yes".localized, style: .default, handler: { (action: UIAlertAction!) in
                
                self.deleteProductRequest(sender);
                
            }));
            
            confirmationAlert.addAction(UIAlertAction(title: "No".localized, style: .default, handler: { (action: UIAlertAction!) in
                //print("Handle Cancel Logic here")
            }));
            
            present(confirmationAlert, animated: true, completion: nil)
        } else {
            // Fallback on earlier versions
        }
        
      
    }
    
    
    @objc func deleteProductRequest(_ sender:UIButton)
    {
        //print("deleteProductRequest");
        //print(sender.tag);
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        let product_id = String(sender.tag);
        
        baseURL = "vendorapi/vproducts/delete/";
        
        print(baseURL);
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&entity_id="+product_id;
        
        print(postString);
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        sendRequest(url: baseURL, parameters: postString);
    }
    
    @objc func fetchProductListing(deliveryString: String)
    {
        print("fetchRelatedProductListing");
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        if isAuction{
            baseURL = "vauctionapi/auction/productlist";
        }
        else
        {
            if(deliveryString == "")
            {
                baseURL = "vendorapi/vproducts/item/";
            }
            else
            {
                baseURL = "vendorapi/vproducts/item/\(deliveryString)"
            }
            
        }
        
        
        print(baseURL);
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&page="+String(page);
        postString += "&store_id="+store_id;
        if(filter != "")
        {
            postString += "&filter="+filter;
        }
        print("-----poststring----\(postString)")
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        sendRequest(url: baseURL, parameters: postString);
    }
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
            
                self.page += 1;
                self.fetchProductListing(deliveryString: deliveryPostString);
                manageProductTableView.reloadData();
            }
            
        }
        
    }
    
}
