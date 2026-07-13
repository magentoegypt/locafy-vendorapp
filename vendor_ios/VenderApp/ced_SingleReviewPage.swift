//
//  ced_SingleReviewPage.swift
//  VenderApp
//
//  Created by cedcoss on 4/13/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_SingleReviewPage: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource {
  
    

    @IBOutlet weak var maintable: UITableView!
    @IBOutlet weak var lblheader: UILabel!
    
    var index = ""
    var data = JSON()
    var statusdictionary = [String : JSON]()
    
 
   
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let param =  ["review_id":"\(index)","vendor_id" : vendorId]
         sendurlrequest(url: "rest//V1/vproductreviewapi/viewReview", para: param)
        maintable.delegate = self
        maintable.dataSource = self
        lblheader.setThemeColor()
        lblheader.text = " Edit Review"
        lblheader.font = UIFont.boldSystemFont(ofSize: 17)
        
        
        // Do any additional setup after loading the view.
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let json = try? JSON(data: data!) else {return}
        // decoding here
         print(json)
        self.data = json
      self.maintable.reloadData()
    }
    
    @objc func productbttnpressed(_ sender:UIButton)
    {
        let storyBoard = UIStoryboard(name: "ProductAddon", bundle: nil)
        
        let viewControl = storyBoard.instantiateViewController(withIdentifier: "addProductAddonFirstPage") as! AddProductAddonFirstPage
        navigationController!.pushViewController(viewControl, animated: true)
        
    }
    
    
    
//    {"data":{"vendor_id":"1","review_id":"348","status_id":"2","nickname":"demo","title":"tretsingfsdf","detail":"test","ratings":"{\"4\":\"19\",\"5\":\"23\"}"}}
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = maintable.dequeueReusableCell(withIdentifier: "SingleReviewCell", for: indexPath) as! ced_SingleReviewPagecell
        cell.jsondata = data
        cell.defaultstatusid = statusdictionary
        cell.index = index
        cell.populatedata()
        //cell.products.addTarget(self, action: #selector(productbttnpressed(_:)), for: .touchUpInside)
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return CGFloat(420)
    }
    
}
