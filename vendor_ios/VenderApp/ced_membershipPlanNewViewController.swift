//
//  ced_membershipPlanNewViewController.swift
//  VenderApp
//
//  Created by cedcoss on 14/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_membershipPlanNewViewController: ced_VendorBaseClass {
    
    //MARK:- stored properties
    
    private var currentPage = 1
    private var loading = true
    var runningPlans = [[String:String]]()
    var memberShipPlans = [[String:String]]()
    
    
    //MARK: - UI Control properties
    
    lazy var TopLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 16, weight: .semibold)
        lbl.textColor = DynamicColor.headingTextColor
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.text = "  "+"Seller Resources".localized + "  "
        lbl.numberOfLines = 1
        lbl.layer.cornerRadius = 3.0
        return lbl
    }()
    
    lazy var historyTable: UITableView = {
        let table = UITableView()
        table.backgroundColor   = .clear
        table.separatorStyle    = .none
        table.translatesAutoresizingMaskIntoConstraints = false
        table.isMultipleTouchEnabled = true
        table.register(ced_historyNewTableViewCell.self, forCellReuseIdentifier: ced_historyNewTableViewCell.reuseID)
        table.register(ced_membershipPlansnewTableViewCell.self, forCellReuseIdentifier: ced_membershipPlansnewTableViewCell.reuseId)
//        table.delegate   = self
//        table.dataSource = self
        return table
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Membership Plans".localized
        configureAndAutolayoutViews()
        makeNetworkCall()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    //MARK:- UIConfig
    
    func configureAndAutolayoutViews(){
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        view.addSubview(TopLabel)
        view.addSubview(historyTable)
        
        if #available(iOS 11.0, *) {
            TopLabel.anchor(top: view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 10, paddingRight: 10, height: 40)
        } else {
            TopLabel.anchor(top: view.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 95, paddingLeft: 10, paddingRight: 10, height: 40)
        }
       
        historyTable.anchor(top: TopLabel.bottomAnchor, left: view.leadingAnchor, bottom: view.bottomAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 10, paddingBottom: 5, paddingRight: 10)
        
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
        self.sendRestFullRequest(url: "plan/items", params: params, store: true)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else {return}
        guard let json = try? JSON(data:data) else {return}
        print(json)
        if requestUrl == "plan/items"{
            //if json[0]["vendor_data"]["status"].boolValue{
                for items in json[0]["vendor_data"]["running_plans"].arrayValue{
                    var tempDict = [String:String]()
                    for (label,Val) in items{
                        tempDict[label] = Val.stringValue
                    }
                    self.runningPlans.append(tempDict)
                }
                for items in json[0]["vendor_data"]["membership_plans"].arrayValue{
                    var tempDict = [String:String]()
                    for (label,Val) in items{
                        tempDict[label] = Val.stringValue
                    }
                    self.memberShipPlans.append(tempDict)
                }
                DispatchQueue.main.async {
                    self.historyTable.dataSource = self
                    self.historyTable.delegate = self
                    self.historyTable.reloadData()
                }
       //     }else{
                if json[0]["vendor_data"]["membership_plans"].count == 0{
                    loading = false
                    if self.currentPage == 1{
                        self.view.makeToast("No Plans Available".localized)
                        return
                    }
                }
//            }
        }
        if requestUrl == "addMembership"{
            if json[0]["data"]["status"].boolValue{
                let customerID = json[0]["data"]["customer_id"].stringValue
                let cartID = json[0]["data"]["cart_id"].stringValue
               
                let vc = ced_membershipWebCheckoutViewController()
                vc.cartId = cartID
                vc.custID = customerID
                self.navigationController?.pushViewController(vc, animated: true)
            }
        }
    }
    
    func resetVariables(){
        self.loading = true
        self.currentPage = 1
        self.runningPlans = [[String:String]]()
        self.memberShipPlans = [[String:String]]()
        makeNetworkCall()
    }

}
//MARK:- tableView delegates

extension ced_membershipPlanNewViewController:UITableViewDataSource,UITableViewDelegate{
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section{
        case 0 :
            return runningPlans.count
        default:
            return 1
        }
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section{
        case 0:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_historyNewTableViewCell.reuseID, for: indexPath) as! ced_historyNewTableViewCell
            cell.parent = self
            cell.isFromMembersship = true
            cell.initViews()
            cell.historyData = self.runningPlans[indexPath.row]
            cell.ContainerView.cardView()
            return cell
            
        default:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_membershipPlansnewTableViewCell.reuseId, for: indexPath) as! ced_membershipPlansnewTableViewCell
            cell.populate(with: memberShipPlans, parent: self)
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.section{
        case 0:
            if runningPlans.count == 0{
                return 0
            }
            return 220
//            return CGFloat(220*runningPlans.count + 48)
        default:
            if memberShipPlans.count == 0{
                return 0
            }
            return CGFloat(memberShipPlans.count/2 + memberShipPlans.count % 2)*325 + 65
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 0{
                let lbl = UILabel()
                lbl.font = .systemFont(ofSize: 16, weight: .semibold)
                lbl.textColor = DynamicColor.headingTextColor
                lbl.backgroundColor = DynamicColor.themeColor
                lbl.text = "  " + "Vendor Running Plans".localized + "  "
                lbl.numberOfLines = 1
                lbl.layer.cornerRadius = 3.0
                return lbl

        }
        return UIView()
    }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0{
            return 40
        }
        return 0
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
