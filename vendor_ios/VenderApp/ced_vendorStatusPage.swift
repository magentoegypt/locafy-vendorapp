//
//  ced_vendorStatusPage.swift
//  VenderApp
//
//  Created by cedcoss on 27/12/17.
//  Copyright © 2017 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_vendorStatusPage: UIViewController {

    @IBOutlet weak var closeButton: UIButton!
    @IBOutlet weak var message: UILabel!
    @IBOutlet weak var venderNamelabel: UILabel!
    var data:JSON? = JSON()
    var vendorName:String? = String()
    override func viewDidLoad() {
        super.viewDidLoad()
        if let data = data {
            message.text = data["data"]["customer"][0]["message"].stringValue
        }
        if let vendor = vendorName {
            venderNamelabel.text = "Hello, " + vendor
        }
        closeButton.addTarget(self, action: #selector(ced_vendorStatusPage.closeButton(_:)), for: .touchUpInside)
        closeButton.clipsToBounds = true;
        closeButton.layer.cornerRadius = (closeButton.frame.width)/2;
    }
    
    @objc func closeButton(_ sender:UIButton){
       
            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
            let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
            self.present(viewControl!, animated: true, completion: nil)
            
            
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    

}
