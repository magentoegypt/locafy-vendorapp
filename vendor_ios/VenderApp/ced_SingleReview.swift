//
//  ced_SingleReview.swift
//  VenderApp
//
//  Created by cedcoss on 5/8/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import Cosmos

class ced_SingleReview: ced_VendorBaseClass {
    
    @IBOutlet weak var lblheader: UILabel!
    @IBOutlet weak var mainview: UIView!
    @IBOutlet weak var mainstack: UIStackView!
    
    @IBOutlet weak var lblvisiibilty: UILabel!
    
    @IBOutlet weak var visibilityview: UIView!
    
    @IBOutlet weak var lblproducts: UILabel!
    @IBOutlet weak var products: UIButton!
    
    @IBOutlet weak var lblauthor: UILabel!
    @IBOutlet weak var author: UILabel!
    @IBOutlet weak var lblsummaryrating: UILabel!
    
    @IBOutlet weak var summaryview: CosmosView!
    
    @IBOutlet weak var lblsituation: UILabel!
    @IBOutlet weak var bttnsituation: UIButton!
    @IBOutlet weak var lblsurname: UILabel!
    @IBOutlet weak var surname: UITextField!
    @IBOutlet weak var evaluationsummary: UILabel!
    @IBOutlet weak var evolution: UITextField!
    
    @IBOutlet weak var optionalstack: UIStackView!
    @IBOutlet weak var optionalstackheight: NSLayoutConstraint!
    
    @IBOutlet weak var Viewheight: NSLayoutConstraint!
    
    
    @IBOutlet weak var savebttn: UIButton!
    @IBOutlet weak var Deletebttn: UIButton!
    
//    @IBOutlet weak var lblclassification: UILabel!
//
//    @IBOutlet weak var lbprice: UILabel!
//
//    @IBOutlet weak var priceview: CosmosView!
//
//    @IBOutlet weak var lblquality: UILabel!
     
//    @IBOutlet weak var qualityview: CosmosView!
    @IBOutlet weak var lblassement: UILabel!
    @IBOutlet weak var assesment: UITextField!
//
//    @IBOutlet weak var lblValue: UILabel!
//    @IBOutlet weak var value: CosmosView!
    
    @IBOutlet weak var Resetbttn: UIButton!
    
    @IBOutlet weak var detailStack: UIStackView!
    @IBOutlet weak var detailedHaight: NSLayoutConstraint!
    
    var jsondata = JSON()
    var index = ""
    var statusdictionary = [String : JSON]()
    var y = 0
    var x = 0
    var z = 0
    var check = true
    var stores = [String]()
    var defaultstoreid = [String : JSON]()
    var defaultstatusid = [String:String]()
    var statusdata = [String:String]()
    var array = [String]()
    var defaultView = false
    var ratingdata = JSON()
    let reviewid = String()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mainview.cardView()
        detailedHaight.constant = 0
        
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let param =  ["review_id":"\(index)","vendor_id" : vendorId]
         sendurlrequest(url: "rest//V1/vproductreviewapi/viewReview", para: param)
        lblheader.setThemeColor()
        lblheader.text = " Edit Review".localized
        if(ced_storeVC.selectLangauge == "ar"){
            lblheader.textAlignment = .right
        }
        lblheader.font = UIFont.boldSystemFont(ofSize: 17)
        bttnsituation.addTarget(self, action:  #selector(situationpressed(_:)), for: .touchUpInside)
        products.addTarget(self, action: #selector(productbttnpressed(_:)), for: .touchUpInside)
        Resetbttn.addTarget(self, action: #selector(resetbttnpressed(_:)), for: .touchUpInside)
        savebttn.setThemeColor()
        Deletebttn.setThemeColor()
        Resetbttn.setThemeColor()
        savebttn.setTitleColor(UIColor.white, for: .normal)
        Deletebttn.setTitleColor(UIColor.white, for: .normal)
        Resetbttn.setTitleColor(UIColor.white, for: .normal)
        // Do any additional setup after loading the view.
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let json = try? JSON(data: data!) else {return}
        // decoding here
        print(requestUrl as Any)
         print(json)
        self.jsondata = json
        if requestUrl == "rest//V1/vproductreviewapi/viewReview"
        {
            resetViewsAndVar()
            populatedata() }
        
        if requestUrl == "rest/V1/vproductreviewapi/saveReview"
        {
            if json[0]["vendor_data"]["status"].stringValue == "true" {
                self.view.makeToast(json[0]["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                    let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "managereviews") as! ced_ManageReviews
                    self.navigationController?.pushViewController(vc, animated: true)
                }
            }
            
        }
        if requestUrl == "rest/V1/vproductreviewapi/deleteReview"
        {
            if json[0]["vendor_data"]["status"].stringValue == "true" {
                self.view.makeToast(json[0]["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                    let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "managereviews") as! ced_ManageReviews
                    self.navigationController?.pushViewController(vc, animated: true)
                }
            }
            
        }
       
    }
    
    func resetViewsAndVar(){
        optionalstack.subviews.forEach {$0.removeFromSuperview()}
        optionalstackheight.constant = 30
        Viewheight.constant = 30
        
        detailStack.subviews.forEach {$0.removeFromSuperview()}
        detailedHaight.constant = 0
        
    }
    
    @objc func resetbttnpressed(_ sender:UIButton)
    {
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let param =  ["review_id":"\(index)","vendor_id" : vendorId]
        
         sendurlrequest(url: "rest//V1/vproductreviewapi/viewReview", para: param)
    }
    
    @objc func situationpressed(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = ["Approved".localized,"Pending".localized,"Not Approved".localized]
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)

        if dropDown.isHidden {
        _ = dropDown.show()
        } else {
        dropDown.hide();
        }
    }
    
    @objc func productbttnpressed(_ sender:UIButton)
    {
        let storyBoard = UIStoryboard(name: "ProductAddon", bundle: nil)
        
        let viewControl = storyBoard.instantiateViewController(withIdentifier: "addProductAddonFirstPage") as! AddProductAddonFirstPage
        navigationController!.pushViewController(viewControl, animated: true)
        
    }
//    "detailed_rating" : [
//             {
//               "rating_code" : "Price",
//               "percent" : "60",
//               "value" : "3"
//             },
//             {
//               "rating_code" : "Price",
//               "percent" : "60",
//               "value" : "3"
//             },
//             {
//               "rating_code" : "Quality",
//               "percent" : "80",
//               "value" : "4"
//             }
//           ],
    
    func populatedata()
    {
        mainview.cardView()
        getStoreView()
        ratingdata = jsondata[0]["vendor_data"]["rating_options"]
        print(jsondata[0]["vendor_data"]["review_info"]["product_name"].stringValue)
        products.setTitle(jsondata[0]["vendor_data"]["review_info"]["product_name"].stringValue, for: .normal)
        products.titleLabel!.text =  jsondata[0]["vendor_data"]["review_info"]["product_name"].stringValue
        author.text = jsondata[0]["vendor_data"]["review_info"]["author"].stringValue
        bttnsituation.setTitle(jsondata[0]["vendor_data"]["review_info"]["status_id"].stringValue, for: .normal)
        surname.text =  jsondata[0]["vendor_data"]["review_info"]["nickname"].stringValue
        evolution.text =  jsondata[0]["vendor_data"]["review_info"]["title"].stringValue
//        lbprice.text = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][0]["rating_code"].stringValue
//        lblquality.text = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][1]["rating_code"].stringValue
//        lblValue.text = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][2]["rating_code"].stringValue
        
        assesment.text = jsondata[0]["vendor_data"]["review_info"]["detail"].stringValue
        let c = (jsondata[0]["vendor_data"]["review_info"]["summary_rating"].doubleValue)*0.05
        summaryview.rating = c
//        priceview.rating = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][0]["value"].doubleValue
//        qualityview.rating = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][1]["value"].doubleValue
//        value.rating = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][2]["value"].doubleValue
        
//            jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][1]["value"].doubleValue
        
        for items in jsondata[0]["vendor_data"]["rating_options"].arrayValue{
            let subView = DetailRatingsView()
            self.detailStack.addArrangedSubview(subView)
            detailedHaight.constant += 33
            subView.key = items["rating-id"].stringValue
            subView.RatingLbl.text = items["rating-code"].stringValue
            subView.ratingView.rating = 0
            if let min = items["option"].arrayValue.first?["value"].stringValue{
                if let doubleMin = Int(min){
                    subView.minVal = doubleMin
                }
            }
//            if let max = items["option"].arrayValue.first?["value"].stringValue{
//                if let doubleMax = Double(max){
//                    subView.ratingView.m = doubleMin
//                }
//            }
            
            jsondata[0]["vendor_data"]["review_info"]["detailed_rating"].arrayValue.forEach{rating in
                if items["rating-code"].stringValue == rating["rating_code"].stringValue{
                    subView.ratingView.rating = rating["percent"].doubleValue/20
                }
            }
            
        }
        
        products.titleLabel?.text = "here"
        defaultstoreid = jsondata[0]["vendor_data"]["store_id"].dictionaryValue
//        if jsondata[0]["vendor_data"]["store_id"].count > 0 {
//            populate()
//        }
//
         
        savebttn.addTarget(self, action: #selector(savebuttonpressed(_:)), for: .touchUpInside)
        Deletebttn.addTarget(self, action: #selector(deletebttnpressed(_:)), for: .touchUpInside)
    }


    @objc func getStoreView()
    {
        
        print("HELLO JSON")
        var temp1=[String:String]()
         
        let val = self.jsondata[0]["vendor_data"]["store_id"].arrayValue

        for item in val
        {
            print(item)
            temp1["label"]=item["label"].string!
            let button = UIButton(frame: CGRect(x: 0, y: 0, width:100 , height: 20))
            if item["value"] == 0
            {
                
                if (self.jsondata[0]["vendor_data"]["review_info"]["stores"][0].stringValue) == "0"
                    {
                        button.isSelected = true
                        button.backgroundColor = UIColor.gray
                        self.array.append(String(button.tag))
                    }
                
                button.setTitle(String(describing: item["label"]), for: UIControl.State())
                button.titleLabel?.font =  UIFont(name: "Roboto-Regular", size: 18)
                button.setTitleColor(.black, for: UIControl.State())
                button.addTarget(self, action: #selector(self.DefaultSelected(_:)),for: .touchUpInside)
                //self.fieldcell1.storeStack.addArrangedSubview(button)
                self.optionalstack.addArrangedSubview(button)
            }
            else
            {
                if item["value"].array?.count != 0
                {
                    button.setTitle(String(describing: temp1["label"]!), for: UIControl.State())
                    button.setTitleColor(.black, for: UIControl.State())
                    button.titleLabel?.font =  UIFont(name: "Roboto-Bold", size: 18)
                    button.contentHorizontalAlignment = UIControl.ContentHorizontalAlignment.left
                    button.layer.cornerRadius = 2
                    self.optionalstackheight.constant += 30
                    self.Viewheight.constant += 30
                   // self.fieldcell1.stackHeight.constant += 20
                    self.optionalstack.addArrangedSubview(button)
                   // self.fieldcell1.storeStack.addArrangedSubview(button)
                    
                    temp1["value"]=item["value"].string
                    for valuesw in item["value"].arrayValue
                    {
                        temp1["label"]=valuesw["label"].string!
                        temp1["value"]=valuesw["value"].string!
                        let button = UIButton(frame: CGRect(x: 0, y: 0, width:100 , height: 20))
                        // Y += 30
                        button.setTitle(String(describing: temp1["label"]!), for: UIControl.State())
                        button.setTitleColor(.black, for: UIControl.State())
                        button.contentHorizontalAlignment = UIControl.ContentHorizontalAlignment.left
                        button.titleLabel?.font =  UIFont(name: "Roboto-Regular", size: 18)
                        button.addTarget(self, action: #selector(self.DefaultSelected(_:)),for: .touchUpInside)
                        button.tag = Int(temp1["value"]!)!
                        
                        let store = Array(self.jsondata[0]["vendor_data"]["review_info"]["stores"].arrayValue)
                            for item in store
                            {
                                if item.stringValue == temp1["value"]
                                {
                                    button.isSelected = true
                                    button.backgroundColor = UIColor.gray
                                    self.array.append(String(button.tag))
                                }
                            }
                       
                        button.layer.cornerRadius = 2
                        self.optionalstackheight.constant += 30
                        self.Viewheight.constant += 30
                        self.optionalstack.addArrangedSubview(button)
        //                                        self.fieldcell1.stackHeight.constant += 20
        //                                        self.fieldcell1.storeStack.addArrangedSubview(button)
                    }
                }
                else
                {
                    let button = UILabel(frame: CGRect(x: 0, y: 0, width:100 , height: 20))
                    button.font =  UIFont(name: "Roboto-Bold", size: 16)
                    button.text =  temp1["label"]!
                    button.textColor = UIColor.black
                    button.textAlignment = NSTextAlignment.left;
                    self.optionalstackheight.constant += 30
                    self.Viewheight.constant += 30
                    self.optionalstack.addArrangedSubview(button)
        //                                    self.fieldcell1.stackHeight.constant += 20
        //                                    self.fieldcell1.storeStack.addArrangedSubview(button)
                }
            }
        }
        
        
         
        }
 
    @objc func DefaultSelected(_ sender:UIButton)
    {
        let sender_id  = String(sender.tag)
        if defaultView
        {
            sender.backgroundColor=UIColor.white
            defaultView = false
            sender.isSelected = false
            for item in array
            {
                if item == sender_id
                {
                    let indexOfA = array.index(of: item)
                    array.remove(at: indexOfA!)
                }
            }
        }
        else
        {
            sender.backgroundColor = UIColor.gray
            defaultView = true
            sender.isSelected = true
            array.append(sender_id)
        }
        print(array)
    }
    
    func populate()
    {
        print(defaultstoreid)
        
        for y in defaultstoreid
        {
            optionalstackheight.constant += 25
            Viewheight.constant += 30
       
            let  radioview = CheckboxButtonView()
            
            optionalstack.addArrangedSubview(radioview)
            radioview.backgroundColor = .yellow
            radioview.checkboxButton.tag = x
            radioview.checkboxButton.setTitle( "\(y.value)" , for: .normal)
            radioview.checkboxButton.setImage(UIImage(named: "unchecked"), for: .normal)
            radioview.checkboxButton.addTarget(self, action: #selector(self.optionbttnpressed(_:)), for: .touchUpInside)
             x = x + 1
            
                print(y.key)
                print(jsondata[0]["vendor_data"]["review_info"]["summary_rating"]["store_id"].stringValue)
                if y.key == jsondata[0]["vendor_data"]["review_info"]["summary_rating"]["store_id"].stringValue
                {
            radioview.checkboxButton.setImage(UIImage(named: "checked"), for: .normal)
                }
        }
         
    }
    
    @objc func optionbttnpressed(_ sender:UIButton)
    {
        
        if check
        {
            sender.setImage(UIImage(named: "checked"), for: .normal)
           let storename = (defaultstoreid["\(sender.tag)"])
            guard stores.contains("\(String(describing: storename))") else {
                stores.append("\(String(describing: storename))")
                 
                check.toggle()
                return
            }
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked"), for: .normal)
        }
        check.toggle()
    }
    
    @objc func savebuttonpressed(_ sender:UIButton)
    {
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        var statusid = ""
        for each in defaultstatusid
        {
            if "\(each.value)" == bttnsituation.titleLabel?.text
            {
                statusid = each.key
            }
        }
        
          
        let nickname = surname.text ?? ""
        let title = evolution.text ?? ""
        let detail = assesment.text ?? ""
        let reviewid = index
//        let pricerating = Int(priceview.rating)
//        let qualityrating = Int(qualityview.rating)
//        let valuerating = Int(value.rating)
//        var price = ""
//        var qulity = ""
//        var value = ""
//
//        if pricerating>0
//        {
//              price = ratingdata[0]["option"][pricerating - 1]["value"].stringValue
//        }
//
//        if qualityrating>0
//        {
//              qulity = ratingdata[2]["option"][qualityrating - 1]["value"].stringValue
//        }
//
//        if valuerating>0
//        {
//              value = ratingdata[1]["option"][valuerating - 1]["value"].stringValue
//        }
        
       
       
       
//        print(pricerating)
        var rating = [String:String]()
       // rating += "{";
        
        detailStack.subviews.forEach {subview in
            if let subview = subview as? DetailRatingsView{
                //managed from code by passing the minimum value and adding minValue with rating
                
                if subview.ratingView.rating != 0{
                    let rtn = subview.minVal - 1
                    //this check is for case where minval could be 1 
                    if Int(subview.ratingView.rating) < rtn{
                        rating[subview.key]=String(Int(subview.ratingView.rating) + rtn)
                    }else{
                        rating[subview.key]=String(Int(subview.ratingView.rating))
                    }
                    
                }
               
            }
        }
//        rating += "\""+"3"+"\":\""+price+"\",";
//        rating += "\""+"2"+"\":\""+value+"\",";
//        rating += "\""+"1"+"\":\""+qulity+"\"}";
         
        print(rating.convtToJson())
        let finalRating = rating.convtToJson() as! String
   
        
        let param =  ["review_id":reviewid,"vendor_id" : vendorId,"status_id": statusid ,"nickname": nickname  ,"title" : title  , "detail":detail ,"ratings" : finalRating ,"stores" : "\(array)"]
        print(param)
        sendurlrequest(url: "rest/V1/vproductreviewapi/saveReview", para: param)
    }
    
    
    @objc func deletebttnpressed(_ sender:UIButton)
    {
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let reviewid = jsondata[0]["vendor_data"]["review_info"]["summary_rating"]["review_id"].stringValue
        let param =  ["review_id":index,"vendor_id" : vendorId ]
         sendurlrequest(url: "rest/V1/vproductreviewapi/deleteReview", para: param)
    }
    
    
    /*
    // MARK: - Navigation
     populatedata()
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
