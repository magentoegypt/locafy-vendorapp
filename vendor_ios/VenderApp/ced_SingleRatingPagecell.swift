//
//  ced_SingleReviewPagecell.swift
//  VenderApp
//
//  Created by cedcoss on 4/3/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_SingleRatingPagecell: UITableViewCell {

    @IBOutlet weak var mainview: UIView!
    @IBOutlet weak var lbldefault: UILabel!
    @IBOutlet weak var lblprice: UILabel!
    
    @IBOutlet weak var lblvisibility: UILabel!
    @IBOutlet weak var lblDuuesy: UILabel!
    
    @IBOutlet weak var Duesy: UILabel!
    @IBOutlet weak var optionView: UIView!
    
    @IBOutlet weak var savebttn : UIButton!
    
    @IBOutlet weak var optionstackview: UIStackView!
    @IBOutlet weak var optionstackheight: NSLayoutConstraint!
    
    var stores = [String]()
    var savedata = JSON()
    var singledata = JSON()
    var index = 0
    var y = 0
    var parent : ced_SingleRatingPage?
     
    var stackheight = CGFloat()
    
     var x = 0
    var check = true
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        mainview.cardView()
        savebttn.addTarget(self, action: #selector(self.savebttnpressed(_:)), for: .touchUpInside)
        savebttn.setThemeColor()
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
//        radioview.radioimg.layer.borderWidth = 1
//        radioview.radioimg.layer.borderColor = UIColor.black.cgColor
//        bttnoption.titleLabel?.textAlignment = .left
    }
    
    func populate()
    {
        
        while(y < singledata["stores"]["DuuEasy"]["DuuEasy"].count)
        {
            print(singledata["stores"]["DuuEasy"]["DuuEasy"][y].stringValue)
            
            optionstackheight.constant += 25
            stackheight += 25
            let  radioview = CheckboxButtonView() 
           
            radioview.checkboxButton.tag = x
             var name = singledata["stores"]["DuuEasy"]["DuuEasy"][y].stringValue
            name.removeFirst()
            name.removeFirst()
            print(name)
            radioview.checkboxButton.setTitle(name , for: .normal)
            radioview.checkboxButton.setImage(UIImage(named: "unchecked"), for: .normal)
            radioview.checkboxButton.addTarget(self, action: #selector(self.optionbttnpressed(_:)), for: .touchUpInside)
             x = x + 1
            var z = 0
            while z < singledata["rating_view"]["stores"].count
            {
                if singledata["stores"]["DuuEasy"]["DuuEasy"][y].stringValue.first == singledata["rating_view"]["stores"][z].stringValue
                {
            radioview.checkboxButton.setImage(UIImage(named: "checked"), for: .normal)
                    stores.append(singledata["rating_view"]["stores"][z].stringValue)
                }
                
                z += 1
            }
             optionstackview.addArrangedSubview(radioview)
            y += 1
        }
        
        
 
    }
    
    @objc func optionbttnpressed(_ sender:UIButton)
    {
        
        if check
        {
            sender.setImage(UIImage(named: "checked"), for: .normal)
              
            guard stores.contains("\(sender.tag+1)") else {
                stores.append("\(sender.tag+1)")

                check.toggle()
                return
            }
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked"), for: .normal)
            for items in 0..<stores.count
            {
                print(stores.count)
                 
                if stores[items] == "\(sender.tag+1)"
                {
                    stores.remove(at: items)
                    break
                }
            }
           
            print(stores)
               
        }
        print(sender.tag)
        check.toggle()
        print(stores)
    }
    
    @objc func savebttnpressed(_ sender:UIButton)
    {
        parsedata()
    }
    
    func parsedata()
    {
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
          
//        [0, 1, 1, 0].map {"\($0)"}.reduce("") { $0 + $1 }
        
        
        
        let param = ["data":["id": "\(index )" ,"vendor_id" : vendorId , "stores" : "\(stores)"]]
        print(param)
            guard let jsondata = try? JSONEncoder().encode(param) else{
                print("data not encoded")
                return
            }
            print(jsondata)
            let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            let url = URL(string: settings.baseUrl + "rest/V1/vproductreviewapi/saveRating")!
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
                
                self.savedata = json
                print(self.savedata)
                    
            }
        }
            task.resume()
        
        }
    
    
}
