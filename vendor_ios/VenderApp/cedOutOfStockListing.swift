//
//  cedOutOfStockListing.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedOutOfStockListing:ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate {

    @IBOutlet weak var headingLabel: UILabel!
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var mainTable: UITableView!
    let refreshControl = UIRefreshControl()
    var page = 1;
    var filter = ""
    var loading = true;
    var products = [[String:String]]()
    
    lazy var filterView:ced_outofstockFilter = {
        let view = ced_outofstockFilter()
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
        headingLabel.text = "Out Of Stock Products".localized
        mainTable.delegate = self;
        mainTable.dataSource = self;
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
        loadProducts()
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        mainTable.refreshControl = refreshControl
        // Do any additional setup after loading the view.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        self.page = 1
        self.products.removeAll()
        mainTable.reloadData()
        loadProducts()
    }

    @objc func loadProducts()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        
       // self.sendRequest(url: "vreportapi/product/outstock", parameters: postString)
        var param = [String:String]()//["vendor_id":vendorId,"hashkey":hashKey,"page":"\(page)"]
        param["vendor_id"] = vendorId
        param["hashkey"] = hashKey
        param["page"] = "\(page)"
        if(filter != ""){
            param["filter"] = filter;
        }
        self.sendRequestWithData(url: "rest/V1/getOutStock", params: param)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["vendor_data"]["success"].stringValue == "true")
            {
                for prod in json["vendor_data"]["product"].arrayValue
                {
                    products.append(["Sku":prod["sku"].stringValue,"Product Name":prod["product_name"].stringValue,"Product Type":prod["product_type"].stringValue,"Quantity":prod["quantity"].stringValue])
                }
                
                
            }
            else
            {
                if(page == 1)
                {
                    products.removeAll()
                    mainTable.reloadData()
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
            cell.qtyLbl.text = "Quantity".localized
            
            let prod = products[indexPath.row];
            cell.productName.text = prod["Product Name"];
            cell.productType.text = prod["Product Type"];
            cell.sku.text = prod["Sku"];
            cell.quantity.text = prod["Quantity"];
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
        return 120;
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
        filterView.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width - 40, height: 225)
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
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading && !self.refreshControl.isRefreshing{
                self.page += 1;
                self.loadProducts()
                mainTable.reloadData();
            }
        }
    }
    
}
