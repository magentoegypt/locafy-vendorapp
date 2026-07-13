//
//  ced_PlanHistoryViewController.swift
//  VenderApp
//
//  Created by Saumya Kashyap on 25/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_PlanHistoryViewController: ced_VendorBaseClass {

    //scrollview
    @IBOutlet weak var scrollerView: UIScrollView!
    @IBOutlet weak var scrollerViewWidth: NSLayoutConstraint!
    //mainview
    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var mainViewheight: NSLayoutConstraint!
    @IBOutlet weak var mainViewwidth: NSLayoutConstraint!
    //vendor resources view
    @IBOutlet weak var vendorResourcesView: UIView!
    @IBOutlet weak var topLabel1: UILabel!
    @IBOutlet weak var productLimit: UILabel!
    @IBOutlet weak var remainingProduct: UILabel!
    //subcription history view
    @IBOutlet weak var subcriptionHistoryView: UIView!
    @IBOutlet weak var topLabel2: UILabel!
    @IBOutlet weak var subcriptionHistoryViewHeight: NSLayoutConstraint!
    @IBOutlet weak var topLabel3: UILabel!
    
    
    
    var product_limit = ""
    var remaining_product=""
    var heightToUse=CGFloat(10)
    var DataView = [[String:String]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        vendorResourcesView.makeCard(vendorResourcesView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        subcriptionHistoryView.makeCard(subcriptionHistoryView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        scrollerViewWidth.constant = self.view.frame.width
        mainViewwidth.constant = scrollerViewWidth.constant
        self.title = "Plan History"
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        //let bodycolorString = Ced_CommonVendor.getInfoPlist("bodyColor") as! String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        //let bodycolor = Ced_CommonVendor.UIColorFromRGB(bodycolorString)
        topLabel1.backgroundColor = color
        topLabel2.backgroundColor = color
        //topLabel3.backgroundColor = color
        loadData()
        // Do any additional setup after loading the view.
    }
    
    
    @objc func loadData(){
        print("view plan history")
        let baseUrl = "vmembershipapi/plan/history/"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        print("-----")
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: baseUrl, parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print("view list json")
            print(json)
            if json["data"]["status"]==true
            {
                product_limit = json["data"]["product_limit"].stringValue
                remaining_product = json["data"]["remaining_products"].stringValue
                let jsondata = json["data"]["membership_history"].array
                for item in jsondata!
                {
                    var dict = [String:String]()
                    for (key,value) in item.dictionary!
                    {
                        dict[key]=value.stringValue
                    }
                    DataView.append(dict)
                }
                loadPageView()
            }
            else{
                let showMsg = json["data"]["message"].stringValue;
                self.view.makeToast(showMsg, duration: 2.0, position: .center)
                Ced_CommonVendor.delay(2.0, closure: {
                    self.navigationController?.popViewController(animated: true)
                })
            }
        }
    }
    @objc func pageselection(_ sender:UIButton)
    {
        print("gfsggb")
        
        let dropDown = DropDown();
        let dropDownToShow = ["10","20","50"]
        
        dropDown.dataSource = dropDownToShow;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    @objc func loadPageView()
    {
        productLimit.text=product_limit
        remainingProduct.text=remaining_product
        heightToUse=topLabel3.frame.height+10
        let itemPageView = ItemPages()
        itemPageView.frame=CGRect(x: 10, y: heightToUse, width: mainViewwidth.constant-40, height: 80)
        heightToUse += itemPageView.frame.height
        subcriptionHistoryView.addSubview(itemPageView)
        itemPageView.itemsNum.text = "\(DataView.count)"+" items"
        itemPageView.pageSelectionButton.addTarget(self, action: #selector(pageselection(_:)), for: .touchUpInside)
        itemPageView.pageSelectionButton.setTitle("10", for: .normal)
        itemPageView.pageSelectionButton.setTitleColor(UIColor.black, for: .normal)
        heightToUse += 10
        
        for item in DataView
        {
            let listView = memberList()
            listView.frame=CGRect(x: 10, y: heightToUse, width: mainViewwidth.constant-40, height: 240)
            
            heightToUse += listView.frame.height+10
            subcriptionHistoryView.addSubview(listView)
            listView.memberListWrapperView.makeCard(listView.memberListWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            
            listView.expiryDate.text=item["expiry_date"]
            listView.memberId.text=item["id"]
            listView.memberName.text=item["name"]
            listView.orderId.text=item["order_id"]
            listView.paymentMethod.text=item["payment_name"]
            listView.startDate.text=item["start_date"]
            listView.status.text=item["status"]
            listView.transactionId.text=item["transaction_id"]
        }
        
        let itemPageView1 = ItemPages()
        itemPageView1.frame=CGRect(x: 10, y: heightToUse, width: mainViewwidth.constant-40, height: 80)
        heightToUse += itemPageView1.frame.height
        subcriptionHistoryView.addSubview(itemPageView1)
        itemPageView1.itemsNum.text = "\(DataView.count)"+" items"
        itemPageView1.pageSelectionButton.addTarget(self, action: #selector(pageselection(_:)), for: .touchUpInside)
        itemPageView1.pageSelectionButton.setTitle("10", for: .normal)
        itemPageView1.pageSelectionButton.setTitleColor(UIColor.black, for: .normal)
        heightToUse += itemPageView1.frame.height+10
        
        subcriptionHistoryViewHeight.constant = heightToUse
        mainViewheight.constant = heightToUse+20
    }
    
    
    
    
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
