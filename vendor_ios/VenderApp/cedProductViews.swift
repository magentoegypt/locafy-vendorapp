//
//  cedProductViews.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa

class cedProductViews: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate, UITextFieldDelegate  {

    @IBOutlet weak var mainTable: UITableView!
    @IBOutlet weak var headingLabel: UILabel!
    @IBOutlet weak var filterButton: UIButton!
    
    var loading = true
    var page = 1;
    var filter = ""
    var products = [[String:String]]()
    var datePickerView = UIDatePicker();
    var disposeBag = DisposeBag()
    
    lazy var filterView:ProductFilterView = {
        let view = ProductFilterView()
        view.DateTo.delegate = self
        view.DateFrom.delegate = self
        view.didSelectFilter = { [weak self] prm in
            print(prm)
            self?.view.viewWithTag(1898)?.removeFromSuperview()
            self?.filter = prm
            self?.page = 1
            self?.loading = true
            self?.products = []
            self?.mainTable.reloadData()
            self?.loadProducts()
        }
        return view
    }()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        headingLabel.setThemeColor()
        headingLabel.text = "Product Views".localized
        mainTable.delegate = self;
        mainTable.dataSource = self;
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
        loadProducts();
        // Do any additional setup after loading the view.
    }

    @objc func loadProducts(){
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String

        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
       // self.sendRequest(url: "vreportapi/product/views", parameters: postString)
        var param = [String:String]()//["vendor_id":vendorId,"hashkey":hashKey,"page":"\(page)"]
        param["vendor_id"] = vendorId
        param["hashkey"] = hashKey
        param["page"] = "\(page)"
        if(filter != ""){
            param["filter"] = filter;
        }
        self.sendRequestWithData(url: "rest/V1/vreport/getViews", params: param)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["vendor_data"]["success"].stringValue == "true")
            {
                for prod in json["success"]["product"].arrayValue
                {
                    products.append(["Sku":prod["sku"].stringValue,"Product Name":prod["product_name"].stringValue,"Product Type":prod["product_type"].stringValue,"Views":prod["views"].stringValue])
                }
            }
            else
            {
                if(page == 1)
                {
                    products.removeAll()
                    mainTable.reloadData();
                   // Alert_File.showValidationError(self,title:"Error".localized,message:json["vendor_data"]["message"].stringValue);
                    self.mainTable.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                }
                loading = false;
                return
            }
            mainTable.restore()
            mainTable.reloadData()
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return products.count;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cedOutOfStockCell", for: indexPath) as? cedOutOfStockCell
        {
            cell.skuLbl.text = "SKU".localized
            cell.productNameLbl.text = "Product Name".localized
            cell.prodTypeLbl.text = "Product Type".localized
            cell.qtyLbl.text = "Views".localized
            
            let prod = products[indexPath.row];
            cell.productName.text = prod["Product Name"];
            cell.productType.text = prod["Product Type"];
            cell.sku.text = prod["Sku"];
            cell.viewsLabel.text = prod["Views"];
            cell.isUserInteractionEnabled = true;
            cell.cellView.cardView();
            return cell;
        }
        return UITableViewCell();
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let storybordOrder = UIStoryboard(name: "ProductAddon", bundle: nil)
        let viewControl = storybordOrder.instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
        self.navigationController?.pushViewController(viewControl, animated: true)
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 160;
    }
    
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        print("filterButtonPressed");
        /* background view */
        
        let bgView = UIView()
        bgView.frame = view.bounds
        view.addSubview(bgView)
        bgView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        bgView.tag = 1898
        self.addgesturesTohideView(self.view)
        
        bgView.addSubview(filterView)
        filterView.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width - 40, height: 260)
        filterView.center = bgView.center
    }
    
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    @objc func hideView(_ recognizer: UITapGestureRecognizer){
        self.view.viewWithTag(1898)?.removeFromSuperview()
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        let view = touch.view ?? UIView()
        return !view.isDescendant(of: filterView)
    }
    
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        
        /* background view */
        let bgCView = UIView();
        bgCView.tag=161;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        let tempInrView = UIView();
        tempInrView.tag=131;
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: UIScreen.main.bounds.width-100,height: CGFloat(270));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.backgroundColor = UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,y: bgCView.frame.size.height / 2);
        
        let label = UILabel();
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        label.text = "Select Date".localized.uppercased();
        label.textAlignment = NSTextAlignment.center;
        label.textColor = UIColor.white;
        label.backgroundColor = color;
        tempInrView.addSubview(label);
        
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.maximumDate = Date()
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
        tempInrView.addSubview(datePickerView);
        
        
        let twoButtonView = TwoButtonView();
        twoButtonView.tag=141;
        twoButtonView.frame = CGRect(x: 0, y: CGFloat(230), width: tempInrView.frame.width,height: CGFloat(40));
        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.addSubview(twoButtonView);
        
        twoButtonView.cancelButton.addTarget(self, action: #selector(cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.cancelButton.backgroundColor = colorRed;
        
        
        //        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        twoButtonView.doneButton.rx.tap.bind{
            let dateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
            dateFormatter.dateStyle = DateFormatter.Style.short
            dateFormatter.timeStyle = DateFormatter.Style.none
            dateFormatter.dateFormat = "dd-MM-yyyy";
            let dateDisplay = dateFormatter.string(from: self.datePickerView.date);
            textField.text = dateDisplay
            textField.resignFirstResponder();
            self.view.viewWithTag(161)?.removeFromSuperview();
        }.disposed(by: disposeBag)
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
        
    }
      
    @objc func cancelDatePickerView(_ sender:UIButton){
        
        sender.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                
                self.page += 1;
                self.loadProducts()
                mainTable.reloadData();
                
            }
            
        }
    }

}
 
 
