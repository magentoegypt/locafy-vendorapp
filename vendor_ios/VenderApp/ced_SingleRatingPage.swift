//
//  ced_SingleReviewPage.swift
//  VenderApp
//
//  Created by cedcoss on 4/3/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_SingleRatingPage: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource {
   
    var jsondat = JSON()
    var index = 0
    var tableheight = CGFloat()
    
      
    @IBOutlet weak var lblprice: UILabel!
    @IBOutlet weak var Filterbttn: UIButton!
    
    @IBOutlet weak var maintable: UITableView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        lblprice.setThemeColor()
        maintable.delegate = self
        maintable.dataSource = self
         
    }
    
    func parsedata( )
    {
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
         
        let param = ["data":["id": "\(index)" ,"vendor_id" : vendorId]]
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
            let url = URL(string: "https://duueasy.com.br/rest/V1/vproductreviewapi/viewRating")!
        print(url)
        print(param)
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
                self.jsondat = json
                print(  self.jsondat[0]["vendor_data"]["rating_view"]["ratin_code"].stringValue)
                    self.maintable.reloadData()
            }
        }
            task.resume()
        
        }
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return jsondat[0]["vendor_data"]["stores"]["DuuEasy"].count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = maintable.dequeueReusableCell(withIdentifier: "SingleratingView", for: indexPath) as! ced_SingleRatingPagecell
        cell.singledata = jsondat[0]["vendor_data"]
        cell.parent = self
        
        
        cell.index = index
        cell.populate()
        tableheight =  cell.stackheight
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 190 + tableheight
    }
  
    
}
