//
//  ced_PincodeCheckerController.swift
//  VenderApp
//
//  Created by cedcoss on 7/2/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_PincodeCheckerController: ced_VendorBaseClass, UIGestureRecognizerDelegate {
    
    @IBOutlet weak var TopLabl: UILabel!
    @IBOutlet weak var filterbttn: UIButton!
    
    @IBOutlet weak var mainTable: UITableView!
    
  var loadMoreData = true
    var currentPage = 1
    
    var Managelist = [JSON]()
    var check:Bool!
    var param = [String : String]()
    var filterbaseview:UIView!
    var filterview = pincodeFilterView()
    var filterparam = String()
    var filter = String();
    var filterzipcode = String()
    var filtercanShip = String()
    var filterCod = String()
    var filterdelivery = String()


    override func viewDidLoad()
    {
        super.viewDidLoad()
        TopLabl.setThemeColor()
        mainTable.delegate = self
        mainTable.dataSource = self
        
        filterbttn.addTarget(self, action:  #selector(filterbttnpressed(_:)), for: .touchUpInside)
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        param["vendor_id"] = vendorId
        param["page"] = "\(currentPage)"
        if check
        {
            TopLabl.text = " Manage List".localized
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            sendRequestWithData(url: "rest/V1/pcapi/getPincodeList", params: param)
        }
        else
        {
            TopLabl.text = " Assigned Pincode".localized
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            sendRequestWithData(url: "rest/V1/pcapi/getAssignedPincodeList", params: param)
        }
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    
    //MARK:- FilterView here
     
    @objc func filterbttnpressed(_ sender:UIButton)
    {
        filterbaseview = UIView()
        filterbaseview.tag = 567
        filterbaseview.backgroundColor = .lightText
        view.addSubview(filterbaseview)
        filterbaseview.translatesAutoresizingMaskIntoConstraints=false
        filterbaseview.topAnchor.constraint(equalTo: view.topAnchor).isActive=true
        filterbaseview.leadingAnchor.constraint(equalTo: view.leadingAnchor).isActive=true
        filterbaseview.trailingAnchor.constraint(equalTo: view.trailingAnchor).isActive=true
        filterbaseview.heightAnchor.constraint(equalTo: view.heightAnchor).isActive=true

        let tap = UITapGestureRecognizer(target: self, action: #selector(self.onSelectbackgroundview(_:)))

        filterbaseview.addGestureRecognizer(tap)
        tap.delegate = self
        //        filterview = pincodeFilterView()
        filterview.tag = 181
        filterview.applybttn.addTarget(self, action: #selector(applybttnpressed(_:)), for: .touchUpInside)
        filterview.resetbttn.addTarget(self, action: #selector(clearbttnpressed(_:)), for: .touchUpInside)
        filterview.Cod.addTarget(self, action: #selector(showDropdown(_:)), for: .touchUpInside)
        filterview.canShip.addTarget(self, action: #selector(showDropdown(_:)), for: .touchUpInside)

        filterbaseview.addSubview(filterview)
        filterview.translatesAutoresizingMaskIntoConstraints=false
        filterview.centerYAnchor.constraint(equalTo: filterbaseview.centerYAnchor).isActive=true
        //  filterview.topAnchor.constraint(equalTo: filterbaseview.topAnchor , constant: 300).isActive=true
        filterview.leadingAnchor.constraint(equalTo: filterbaseview.leadingAnchor , constant: 30).isActive=true
        filterview.trailingAnchor.constraint(equalTo: filterbaseview.trailingAnchor ,constant: -30).isActive=true
        filterview.heightAnchor.constraint(equalTo: filterbaseview.heightAnchor ,multiplier: 0.3).isActive=true
        
    }
    
    @objc func showDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown()

        dropDown.dataSource = ["Yes".localized,"No".localized]


        dropDown.selectionAction = {(index, item) in
        sender.setTitle(item, for: UIControl.State())
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
        dropDown.show()
        } else {
        dropDown.hide()
        }
    }
    @objc func applybttnpressed(_ sender:UIButton)
    {
        print("apply bttn pressed")
        
        filterzipcode = ""
        filtercanShip = ""
        filterCod = ""
        filterdelivery = ""
        
        
        
        if filterview.zipcode.text != ""
        {
            filterzipcode = filterview.zipcode.text!
           
        }
        if filterview.canShip.titleLabel?.text != "----Select----".localized
        {
            if filterview.canShip.titleLabel?.text == "Yes".localized
            {
                filtercanShip.append("1")
                
            }
            else
            {
                filtercanShip.append("0")
            }
           
        }
        if filterview.Cod.titleLabel?.text != "----Select----".localized
        {
            if filterview.Cod.titleLabel?.text == "Yes".localized
            {
                filterCod.append("1")
               
            }
            else
            {
                filterCod.append("0")
                
            }
            
        }
        if filterview.delivery.text != ""
        {
            filterdelivery = filterview.delivery.text!
            
        }
        
        filter = "";
        filter += "{";
        filter += "\""+"zipcode"+"\":\""+filterzipcode+"\",";
        filter += "\""+"can_cod"+"\":\""+filterCod+"\",";
        filter += "\""+"can_ship"+"\":\""+filtercanShip+"\",";
        filter += "\""+"days_to_deliver"+"\":\""+filterdelivery;
        filter += "\"}";
         
     
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        param["vendor_id"] = vendorId
        currentPage = 1
        loadMoreData = true
        Managelist = []
        mainTable.reloadData()
        param["page"] = "\(currentPage)"
        if filter != ""
        {
            param["filter"] = filter
        }
      
        if check
        {
            TopLabl.text = " Manage List".localized
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            sendRequestWithData(url: "rest/V1/pcapi/getPincodeList", params: param)
        }
        else
        {
            TopLabl.text = " Assigned Pincode".localized
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            sendRequestWithData(url: "rest/V1/pcapi/getAssignedPincodeList", params: param)
        }
        filterbaseview.removeFromSuperview()
    }
    
    @objc func clearbttnpressed(_ sender:UIButton)
    {
        filterview = pincodeFilterView()
        filter=""
        filterbaseview.removeFromSuperview()
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        var parameter = [String:String]()
        parameter["vendor_id"] = vendorId
        currentPage = 1
        loadMoreData = true
        Managelist = []
        mainTable.reloadData()
        parameter["page"] = "\(currentPage)"
        if check
        {
            TopLabl.text = " Manage List".localized
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            sendRequestWithData(url: "rest/V1/pcapi/getPincodeList", params: parameter)
        }
        else
        {
            TopLabl.text = " Assigned Pincode".localized
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            sendRequestWithData(url: "rest/V1/pcapi/getAssignedPincodeList", params: parameter)
        }
    }
    
    @objc func onSelectbackgroundview (_ sender:UITapGestureRecognizer)
    {
        
        filterbaseview.removeFromSuperview()
         
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        
        guard let data = data else {return}
        guard var json = try? JSON(data: data) else {return}
        print(json)
        print(requestUrl)
        if requestUrl == "rest/V1/pcapi/getPincodeList"
        {
           
            if json["vendor_data"]["success"].stringValue == "true"
            {
                json["vendor_data"]["pincode_list"].arrayValue.forEach{Managelist.append($0)}
                
            }else{
                loadMoreData = false
                if currentPage == 1{
                  //  self.view.makeToast(json["vendor_data"]["message"].stringValue)
                    self.mainTable.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                }
            }
            mainTable.reloadData()
        }
        if requestUrl == "rest/V1/pcapi/getAssignedPincodeList"
        {
//            if json["vendor_data"]["success"].boolValue
//            {
            if json["vendor_data"]["success"].boolValue
            {
                json["vendor_data"]["pincode_list"].arrayValue.forEach{Managelist.append($0)}
                mainTable.reloadData()
            }else{
                loadMoreData = false
                if currentPage == 1{
                   // self.view.makeToast(json["vendor_data"]["message"].stringValue)
                    self.mainTable.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                }
            }
        }
        if requestUrl == "rest/V1/pcapi/getPincodeDelete"
        {
            if json["vendor_data"]["success"].boolValue
            {
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
                Managelist = []
                loadMoreData = true
                currentPage = 1
                param["page"] = "\(currentPage)"
                filter = ""
                mainTable.reloadData()
          sendRequestWithData(url: "rest/V1/pcapi/getAssignedPincodeList", params: param)
            }
        }
        if requestUrl == "rest/V1/pcapi/addPincode"
        {
            if json["vendor_data"]["success"].boolValue
            {
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
                Managelist = []
                loadMoreData = true
                currentPage = 1
                param["page"] = "\(currentPage)"
                filter = ""
                mainTable.reloadData()
          sendRequestWithData(url: "rest/V1/pcapi/getPincodeList", params: param)
            }
        }
        
    }
    
    
    @objc func addbttnpressed (_ sender:UIButton)
    {
       
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        
        var params = [String:String]()
        
       
        
        if check
        {
            let id = Managelist[sender.tag]["id"].stringValue
            params["vendor_id"] = vendorId
            params["pincode_id"] = id
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        sendRequestWithData(url: "rest/V1/pcapi/addPincode", params: params)
        }
        else
        {
            let id = Managelist[sender.tag]["id"].stringValue
            params["vendor_id"] = vendorId
            params["allowed_id"] = id
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            sendRequestWithData(url: "rest/V1/pcapi/getPincodeDelete", params: params)
        }
    }

    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if(touch.view!.isDescendant(of: filterview))
        {
            return false;
        }
     
        return true;
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

// table view Extention

extension ced_PincodeCheckerController : UITableViewDelegate ,UITableViewDataSource
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Managelist.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = mainTable.dequeueReusableCell(withIdentifier: "pincodechecker", for: indexPath) as! ced_pincodecheckerCell
        cell.data = Managelist[indexPath.row]
        cell.populate()
        if check == false
        {
            cell.AddBttn.setTitle("Delete".localized, for: .normal)
        }else{
            cell.AddBttn.setTitle("ADD".localized, for: .normal)
        }
        cell.AddBttn.tag = indexPath.row
        cell.AddBttn.addTarget(self, action:  #selector(addbttnpressed(_:)), for: .touchUpInside)
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 200
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loadMoreData{
                self.currentPage += 1;
                let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                let vendorId = userData["vendorId"] as! String
                param["vendor_id"] = vendorId
                param["page"] = "\(currentPage)"
                if check
                {
                    Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
                    sendRequestWithData(url: "rest/V1/pcapi/getPincodeList", params: param)
                }
                else
                {
                    Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
                    sendRequestWithData(url: "rest/V1/pcapi/getAssignedPincodeList", params: param)
                }
            }
        }
    }
}
