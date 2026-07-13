//
//  ced_SingleReviewPagecell.swift
//  VenderApp
//
//  Created by cedcoss on 4/13/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import Cosmos

class ced_SingleReviewPagecell: UITableViewCell {

    
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
    
    @IBOutlet weak var savebttn: UIButton!
    @IBOutlet weak var Deletebttn: UIButton!
    
    @IBOutlet weak var lblclassification: UILabel!
    
    @IBOutlet weak var lbprice: UILabel!
    
    @IBOutlet weak var priceview: CosmosView!
    
    @IBOutlet weak var lblquality: UILabel!
     
    @IBOutlet weak var qualityview: CosmosView!
    @IBOutlet weak var lblassement: UILabel!
    @IBOutlet weak var assesment: UITextField!
    
    
    var jsondata = JSON()
    var index = ""
    var parent : ced_SingleReviewPage?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
       
        //mainview.backgroundColor = .red
        bttnsituation.addTarget(self, action:  #selector(situationpressed(_:)), for: .touchUpInside)
        
        populatedata()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func populatedata()
    {
        mainview.cardView()
        print(jsondata[0]["vendor_data"]["review_info"]["product_name"].stringValue)
        products.setTitle(jsondata[0]["vendor_data"]["review_info"]["product_name"].stringValue, for: .normal)
        products.titleLabel!.text =  jsondata[0]["vendor_data"]["review_info"]["product_name"].stringValue
        author.text = jsondata[0]["vendor_data"]["review_info"]["author"].stringValue
        bttnsituation.setTitle(jsondata[0]["vendor_data"]["review_info"]["status_id"].stringValue, for: .normal)
        surname.text =  jsondata[0]["vendor_data"]["review_info"]["nickname"].stringValue
        evolution.text =  jsondata[0]["vendor_data"]["review_info"]["title"].stringValue
        lbprice.text = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][0]["rating_code"].stringValue
        lblquality.text = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][1]["rating_code"].stringValue
        assesment.text = jsondata[0]["vendor_data"]["review_info"]["detail"].stringValue
        let c = (jsondata[0]["vendor_data"]["review_info"]["summary_rating"]["percent"].doubleValue)*0.05
        summaryview.rating = c
        priceview.rating = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][0]["value"].doubleValue
        qualityview.rating = jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][1]["value"].doubleValue
//            jsondata[0]["vendor_data"]["review_info"]["detailed_rating"][1]["value"].doubleValue
        products.titleLabel?.text = "here"
        if jsondata[0]["vendor_data"]["store_id"].count > 0 {
            populate()
        }
        
         
        savebttn.addTarget(self, action: #selector(savebuttonpressed(_:)), for: .touchUpInside)
        Deletebttn.addTarget(self, action: #selector(deletebttnpressed(_:)), for: .touchUpInside)
    }
    var y = 0
    var stackheight = CGFloat()
    var x = 0
    var check = true
    var z = 0
    var stores = [String]()
    var defaultstoreid = [String : JSON]()
    var defaultstatusid = [String : JSON]()
    func populate()
    {
        print(defaultstoreid)
        defaultstoreid = jsondata[0]["vendor_data"]["store_id"].dictionaryValue
        for y in defaultstoreid
        {
            optionalstackheight.constant += 25
            stackheight += 25
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
    @objc func situationpressed(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = ["Approved","Pending","Not Approved"]
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
        let pricerating = "\(priceview.rating)"
        let qualityrating = "\(qualityview.rating)"
        print(pricerating)
        var rating = ""
        rating += "{";
        rating += "\""+"1"+"\":\""+pricerating+"\",";
        rating += "\""+"3"+"\":\""+qualityrating+"\"}";
       
        
        
        let param =  ["review_id":reviewid,"vendor_id" : vendorId,"status_id": statusid ,"nickname": nickname  ,"title" : title  , "detail":detail ,"ratings" : rating]
        print(param)
        guard let jsondata = try? JSONEncoder().encode(param) else{
            print("data not encoded")
            return
        }
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let url = URL(string: settings.baseUrl + "rest/V1/vproductreviewapi/saveReview")!
        var request = URLRequest(url: url)
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        request.httpBody = jsondata
        request.httpMethod = "POST"
    
        let task = session.dataTask(with: request) { (data, urlresponse, error) in
            if error != nil {
                print("error in error" ,error?.localizedDescription)
                            return
                        }
            guard let response = urlresponse as? HTTPURLResponse, (200 ..< 299) ~= response.statusCode else {
             
                print("Error: HTTP request failed \(urlresponse)")
                return
            }
            guard let data = data else {
               
                print("data not recived")
                return
            }
            
            DispatchQueue.main.async {
            guard let json = try? JSON(data: data) else {return}
            // decoding here
             print(json)
            
            
            self.parent?.view.makeToast(json[0]["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
            
            
                
        }
    }
        task.resume()
    
    }
    
    
    @objc func deletebttnpressed(_ sender:UIButton)
    {
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let reviewid = jsondata[0]["vendor_data"]["review_info"]["summary_rating"]["review_id"].stringValue
        let param =  ["review_id":reviewid,"vendor_id" : vendorId ]
        print(param)
        guard let jsondata = try? JSONEncoder().encode(param) else{
            print("data not encoded")
            return
        }
        let sessionConfig = URLSessionConfiguration.default
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let url = URL(string: settings.baseUrl + "rest/V1/vproductreviewapi/deleteReview")!
        var request = URLRequest(url: url)
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsondata
        request.httpMethod = "POST"
        let task = session.dataTask(with: request) { (data, urlresponse, error) in
            if error != nil {
                print("error in error" ,error?.localizedDescription)
                return
            }
            guard let response = urlresponse as? HTTPURLResponse, (200 ..< 299) ~= response.statusCode else {
                print("Error: HTTP request failed \(urlresponse)")
                return
            }
            guard let data = data else {
                print("data not recived")
                return
            }
            DispatchQueue.main.async {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                self.parent?.view.makeToast(json[0]["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
            }
        }
        task.resume()
    }
    
    

}
