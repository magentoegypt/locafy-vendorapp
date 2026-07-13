//
//  ced_MemberShipPlansController.swift
//  VenderApp
//
//  Created by Saumya Kashyap on 24/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_MemberShipPlansController: ced_VendorBaseClass {

    //scrollview
    @IBOutlet weak var scrollerView: UIScrollView!
    @IBOutlet weak var scrollerViewWidth: NSLayoutConstraint!
    //main view
    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var mainViewWidth: NSLayoutConstraint!
    @IBOutlet weak var mainViewHeight: NSLayoutConstraint!
    //vendor running plans view
    @IBOutlet weak var runningPlanView: UIView!
    @IBOutlet weak var runningPlanViewWidth: NSLayoutConstraint!
    @IBOutlet weak var runningPlanViewHeight: NSLayoutConstraint!
    @IBOutlet weak var topLabel1: UILabel!
    //vendor membership plan
    @IBOutlet weak var membershipPlanView: UIView!
    @IBOutlet weak var membershipPlanViewHeight: NSLayoutConstraint!
    @IBOutlet weak var topLabel2: UILabel!
    var heightToUse=CGFloat(10)
    var DataView = [[String:String]]()
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "MemberShip Plans"
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topLabel1.backgroundColor = color
        topLabel2.backgroundColor = color
        runningPlanView.makeCard(runningPlanView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        mainView.makeCard(mainView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        scrollerViewWidth.constant = self.view.frame.width
        mainViewWidth.constant = scrollerViewWidth.constant

        runningPlanViewWidth.constant = 0
        runningPlanViewWidth.constant += mainViewWidth.constant-20

        heightToUse=topLabel1.frame.height+10
        loadData()
        
        // Do any additional setup after loading the view.
    }

    @objc func pageselection(_ sender:UIButton)
    {
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
    
    @objc func loadData(){
        print("view membership")
        let baseUrl = "vmembershipapi/plan/items/"
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
        Alert_File.removeLoadingIndicator(self)
        if let data = data
        {
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if json["data"]["status"]==true
            {
                let jsonData=json["data"]["items"].array
                for item in jsonData!
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
            else
            {
                let showMsg = json["data"]["message"].stringValue;
                self.view.makeToast(showMsg, duration: 2.0, position: .center)
                Ced_CommonVendor.delay(2.0, closure: {
                    self.navigationController?.popViewController(animated: true)
                })
            }
        }
    }
    @objc func loadPageView()
    {
        print("dxsfe")
        let itemPageView = ItemPages()
        itemPageView.frame=CGRect(x: 10, y: heightToUse, width: mainViewWidth.constant-40, height: 80)
        heightToUse += itemPageView.frame.height
        runningPlanView.addSubview(itemPageView)
        itemPageView.itemsNum.text = "\(DataView.count)"+" items"
        itemPageView.pageSelectionButton.addTarget(self, action: #selector(pageselection(_:)), for: .touchUpInside)
        itemPageView.pageSelectionButton.setTitle("10", for: .normal)
        itemPageView.pageSelectionButton.setTitleColor(UIColor.black, for: .normal)
        heightToUse += 10
        for item in DataView
        {
            let listView = memberList()
            listView.frame=CGRect(x: 10, y: heightToUse, width: mainViewWidth.constant-40, height: 240)
            heightToUse += listView.frame.height+10
            runningPlanView.addSubview(listView)
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
        runningPlanViewHeight.constant = heightToUse
        mainViewHeight.constant = heightToUse+20
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
