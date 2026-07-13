//
//  ced_planHistoryNewViewController.swift
//  VenderApp
//
//  Created by cedcoss on 14/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_planHistoryNewViewController: ced_VendorBaseClass {
    
    //MARK:- stored properties
    
    private var currentPage = 1
    private var loading = true
    var HistoryData = [[String:String]]()
    var remainingProduct = ""
    var ProductLimit = ""
    
    
    //MARK: - UI Control properties
    
    lazy var sellerResourcesLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 16, weight: .semibold)
        lbl.textColor = DynamicColor.headingTextColor
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.text = "  "+"Seller Resources".localized  + "  "
        lbl.numberOfLines = 1
        lbl.layer.cornerRadius = 3.0
        return lbl
    }()
    lazy var prodLimitLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 15, weight: .semibold)
        lbl.textColor = DynamicColor.labelColor
        lbl.text = "Product Limit".localized
        lbl.numberOfLines = 1
        return lbl
    }()
    lazy var remainProdLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 15, weight: .semibold)
        lbl.textColor = DynamicColor.labelColor
        lbl.text = "Remaining Products".localized
        lbl.numberOfLines = 1
        return lbl
    }()
    lazy var topLabelStack:UIStackView = {
        let stack = UIStackView()
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        stack.spacing = 5
        stack.alignment = .fill
        return stack
    }()
    lazy var sellerSubscriptionLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 16, weight: .semibold)
        lbl.textColor = DynamicColor.headingTextColor
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.text = "  " + "Seller Subscription History".localized
        lbl.numberOfLines = 1
        lbl.layer.cornerRadius = 3.0
        return lbl
    }()
    
    
    lazy var historyTable: UITableView = {
        let table = UITableView()
        table.backgroundColor   = .clear
        table.separatorStyle    = .none
        table.translatesAutoresizingMaskIntoConstraints = false
        table.register(ced_historyNewTableViewCell.self, forCellReuseIdentifier: ced_historyNewTableViewCell.reuseID)
//        table.delegate   = self
//        table.dataSource = self
        return table
    }()

    //MARK:- Life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Plans History".localized
        configureAndAutolayoutViews()
        makeNetworkCall()
        
        
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    //MARK:- UIConfig
    
    func configureAndAutolayoutViews(){
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        view.addSubview(sellerResourcesLabel)
        view.addSubview(topLabelStack)
        topLabelStack.addArrangedSubview(prodLimitLabel)
        topLabelStack.addArrangedSubview(remainProdLabel)
        view.addSubview(sellerSubscriptionLabel)
        view.addSubview(historyTable)
        
        if #available(iOS 11.0, *) {
            sellerResourcesLabel.anchor(top: view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 10, paddingRight: 10, height: 40)
        } else {
            sellerResourcesLabel.anchor(top: view.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 95, paddingLeft: 10, paddingRight: 10, height: 40)
        }
        topLabelStack.anchor(top: sellerResourcesLabel.bottomAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 0, paddingLeft: 10, paddingRight: 10, height: 40)
        sellerSubscriptionLabel.anchor(top: topLabelStack.bottomAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 8, paddingLeft: 10, paddingRight: 10, height: 40)
        historyTable.anchor(top: sellerSubscriptionLabel.bottomAnchor, left: view.leadingAnchor, bottom: view.bottomAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 10, paddingBottom: 5, paddingRight: 10)
        
    }
    
    //MARK:- Netwoking Section
    
    func makeNetworkCall(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
     //   let hashKey    = userData["hashKey"] as! String
        var params = [String:String]()
        params["vendor_id"] = vendorId
        params["page"] = "\(currentPage)"
        self.sendRestFullRequest(url: "plan/history", params: params, store: true)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else {return}
        guard let json = try? JSON(data:data) else {return}
        print(json)
        
        if json[0]["vendor_data"]["status"].stringValue == "true"{
            if currentPage == 1{
                self.remainingProduct = json[0]["vendor_data"]["remaining_products"].stringValue
                self.ProductLimit = json[0]["vendor_data"]["product_limit"].stringValue
            }
           
            for items in json[0]["vendor_data"]["membership_history"].arrayValue{
                var tempDict = [String:String]()
                for (label,Val) in items{
                    tempDict[label] = Val.stringValue
                }
                self.HistoryData.append(tempDict)
            }
            
            DispatchQueue.main.async {
                self.remainProdLabel.text = "  "+"Remaining Products".localized + " : " + self.remainingProduct
                self.prodLimitLabel.text = "  "+"Product Limit".localized + " : " + self.ProductLimit
                self.historyTable.dataSource = self
                self.historyTable.delegate = self
                self.historyTable.reloadData()
            }
            
        }else{
            self.loading = false
            if currentPage == 1 {
                self.remainingProduct = json[0]["vendor_data"]["remaining_products"].stringValue
                self.ProductLimit = json[0]["vendor_data"]["product_limit"].stringValue
               // self.view.makeToast("No Plans Available".localized)
                self.historyTable.setEmptyMessage("No Plans Available".localized)
                DispatchQueue.main.async {
                    self.remainProdLabel.text = "  "+"Remaining Products".localized + " : " + self.remainingProduct
                    self.prodLimitLabel.text = "  "+"Product Limit".localized + " : " + self.ProductLimit
                }
                return
            }
        }
    }
    
}
//MARK:- TableView delegates

extension ced_planHistoryNewViewController:UITableViewDataSource,UITableViewDelegate{
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return HistoryData.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ced_historyNewTableViewCell.reuseID, for: indexPath) as! ced_historyNewTableViewCell
        cell.parent = self
        cell.initViews()
        cell.historyData = self.HistoryData[indexPath.row]
        cell.ContainerView.cardView()
        cell.selectionStyle = .none
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 220
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                self.currentPage += 1;
                self.makeNetworkCall()
            }
        }
    }
}

