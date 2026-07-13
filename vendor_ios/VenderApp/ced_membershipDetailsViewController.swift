//
//  ced_membershipDetailsViewController.swift
//  VenderApp
//
//  Created by cedcoss on 21/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_membershipDetailsViewController: ced_VendorBaseClass {
    
    var memberShipID = ""
    var memberShipNameValue = ""
    
    //MARK:- Stored properties
    
    var planImage = ""
    var planDuration = ""
    var AllowedProducts = ""
    var price = ""
    var specialPrice = ""
    var addtoCartEnable = false
    var allowedCategories = [String]()
    
    
    //MARK:- UI Elements
    
    lazy var topHeading:UILabel = {
        let lbl = UILabel()
        lbl.textAlignment = .center
        lbl.font = .systemFont(ofSize: 16, weight: .semibold)
        lbl.textColor = DynamicColor.headingTextColor
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.text = "Membership Details".localized
        lbl.numberOfLines = 1
        lbl.layer.cornerRadius = 3.0
        return lbl
    }()
    lazy var membershipName:UILabel = {
        let lbl = UILabel()
        lbl.textAlignment = .center
        lbl.font = .systemFont(ofSize: 16, weight: .semibold)
        lbl.textColor = DynamicColor.labelColor
        lbl.text = memberShipNameValue
        return lbl
    }()
    
    
    lazy var detailsTable:UITableView = {
        let table = UITableView()
        table.backgroundColor   = .clear
        table.separatorStyle    = .none
        table.translatesAutoresizingMaskIntoConstraints = false
        table.register(ced_membershipAllowedCategoryTableViewCell.self, forCellReuseIdentifier: ced_membershipAllowedCategoryTableViewCell.reuseID)
        table.register(ced_memberShipImageTableViewCell.self, forCellReuseIdentifier: ced_memberShipImageTableViewCell.reuseID)
        table.register(ced_membershipLabelValueStackTableViewCell.self, forCellReuseIdentifier: ced_membershipLabelValueStackTableViewCell.reuseID)
//        table.delegate   = self
//        table.dataSource = self
        return table
    }()
    
    lazy var bottomStack:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        return stack
    }()
    
    lazy var subscribeLabel:UILabel = {
        let lbl = UILabel()
        lbl.textAlignment = .center
        lbl.font = .systemFont(ofSize: 15, weight: .semibold)
        lbl.textColor = DynamicColor.labelColor
        lbl.text = "Subscribe This One".localized
        lbl.numberOfLines = 1
        return lbl
    }()
    
    lazy var AddToCart:UIButton = {
        let label = UIButton(type: .system)
        label.setTitle("Already Subscribed".localized, for: .normal)
        label.isEnabled = false
        label.backgroundColor = DynamicColor.themeColor
        label.setTitleColor( DynamicColor.headingTextColor, for: .normal)
        label.titleLabel?.font = .systemFont(ofSize: 15, weight: .semibold)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.addTarget(self, action: #selector(addToCartTapped(_:)), for: .touchUpInside)
        return label
    }()
    
    
    //MARK:- Life Cycle

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Membership Details".localized
        configureAndAutolayoutView()
        makeNetworkCall()
    }
    
    //MARK:- UI Helpers
    
    func configureAndAutolayoutView(){
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        view.addSubview(topHeading)
        view.addSubview(detailsTable)
        bottomStack.addSubViews([subscribeLabel,AddToCart], in: bottomStack)
        view.addSubview(bottomStack)
        view.addSubview(membershipName)
        
        
        if #available(iOS 11.0, *) {
            topHeading.anchor(top: view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 10, paddingRight: 10, height: 40)
        } else {
            topHeading.anchor(top: view.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 95, paddingLeft: 10, paddingRight: 10, height: 40)
        }
        membershipName.anchor(top: topHeading.bottomAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 0, paddingLeft: 10, paddingRight: 10, height: 30)
        detailsTable.anchor(top: membershipName.bottomAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 8, paddingLeft: 10, paddingRight: 10)
        bottomStack.anchor(top: detailsTable.bottomAnchor, left: view.leadingAnchor, bottom: view.bottomAnchor, right: view.trailingAnchor, paddingTop: 0, paddingLeft: 10, paddingBottom: 15, paddingRight: 10, height: 40)
        
    }
    
    //MARK:- selectors
    
    @objc func addToCartTapped(_ sender : UIButton){
        print("in here too")
        Alert_File.addLoadingIndicator(parent ?? UIViewController(), msg: "Loading Please Wait...".localized)
            print("in here")
            let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
         //   let hashKey    = userData["hashKey"] as! String
            var params = [String:String]()
            params["vendor_id"] = vendorId
            params["membership_id"] = memberShipID
            self.sendRestFullRequest(url: "addMembership", params: params, store: false)
    }
    
    //MARK:- API section
    
    func makeNetworkCall(){
        if memberShipID == ""{
            self.view.makeToast("No membership id found".localized)
            return
        }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        var params = [String:String]()
        params["vendor_id"] = vendorId
        params["membership_id"] = memberShipID
        self.sendRestFullRequest(url: "viewMembership", params: params, store: true)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else{return}
        guard let json = try? JSON(data:data) else {return}
        print(json)
        if requestUrl == "addMembership"{
            if json[0]["data"]["status"].boolValue{
                let customerID = json[0]["data"]["customer_id"].stringValue
                let cartID = json[0]["data"]["cart_id"].stringValue
               
                let vc = ced_membershipWebCheckoutViewController()
                vc.cartId = cartID
                vc.custID = customerID
                self.navigationController?.pushViewController(vc, animated: true)
            }
        }else{
            if json[0]["data"]["status"].boolValue{
                self.AllowedProducts = json[0]["data"]["membership_info"]["allowed_products"].stringValue
                json[0]["data"]["membership_info"]["allowed_categories"].arrayValue.forEach {self.allowedCategories.append($0.stringValue)}
                self.planDuration = json[0]["data"]["membership_info"]["duration"].stringValue
                self.planImage = json[0]["data"]["membership_info"]["image"].stringValue
                self.price = json[0]["data"]["membership_info"]["price"].stringValue
                self.specialPrice = json[0]["data"]["membership_info"]["special_price"].stringValue
                if json[0]["data"]["membership_info"]["show_addtocart"].stringValue == "true"{
                    self.addtoCartEnable = true
                }
                DispatchQueue.main.async {
                    if self.addtoCartEnable{
                        self.AddToCart.isEnabled = true
                        self.AddToCart.setTitle("Add to Cart".localized, for: .normal)
                    }
                    self.detailsTable.dataSource = self
                    self.detailsTable.delegate = self
                    self.detailsTable.reloadData()
                }
            }
        }
       
    }

}

//MARK:- UITableview Delegates

extension ced_membershipDetailsViewController:UITableViewDataSource,UITableViewDelegate{
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 6
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_memberShipImageTableViewCell.reuseID, for: indexPath) as! ced_memberShipImageTableViewCell
            cell.imagePlan = self.planImage
            cell.selectionStyle = .none
            return cell
        case 1:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_membershipLabelValueStackTableViewCell.reuseID, for: indexPath) as! ced_membershipLabelValueStackTableViewCell
            cell.leftLabelText = "Duration of Package".localized
            cell.rightLabelText = self.planDuration
            cell.selectionStyle = .none
            return cell
        case 2:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_membershipLabelValueStackTableViewCell.reuseID, for: indexPath) as! ced_membershipLabelValueStackTableViewCell
            cell.leftLabelText = "Allowed No of Products".localized
            cell.rightLabelText = self.AllowedProducts
            cell.selectionStyle = .none
            return cell
        case 3:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_membershipLabelValueStackTableViewCell.reuseID, for: indexPath) as! ced_membershipLabelValueStackTableViewCell
            cell.leftLabelText = "Price".localized
            cell.rightLabelText = self.price
            cell.selectionStyle = .none
            return cell
        case 4:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_membershipLabelValueStackTableViewCell.reuseID, for: indexPath) as! ced_membershipLabelValueStackTableViewCell
            cell.leftLabelText = "Special Price".localized
            cell.rightLabel.textColor = .red
            cell.leftLabel.textColor = .red
            cell.rightLabelText = self.specialPrice
            cell.selectionStyle = .none
            return cell
        case 5:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_membershipAllowedCategoryTableViewCell.reuseID, for: indexPath) as! ced_membershipAllowedCategoryTableViewCell
            cell.categoryItems = self.allowedCategories
            cell.selectionStyle = .none
            return cell
        default:
            return UITableViewCell()
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.row {
        case 0:
            return 250
        case 1,2,3:
            return 40
        case 4:
            if self.specialPrice == ""{
                return 0
            }
            return 40
        case 5:
            return CGFloat(self.allowedCategories.count*35 + 40)
        default:
            return 0
        }
    }
    
}
