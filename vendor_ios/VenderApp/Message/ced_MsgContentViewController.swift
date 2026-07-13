//
//  ced_MsgContentViewController.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_MsgContentViewController: ced_VendorBaseClass {
    @IBOutlet weak var scroll: UIScrollView!
    
    
    @IBOutlet weak var ViewWidth: NSLayoutConstraint!
    @IBOutlet weak var Date: UILabel!
    
    @IBOutlet weak var subject: UILabel!
    
    @IBOutlet weak var compose: UIButton!
    @IBOutlet weak var message: UILabel!
    
    var date1 = ""
    var subject1 = ""
    var message1 = ""
    
    @IBOutlet weak var textView1: UITextView!
    
    @IBOutlet weak var MessageContent: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        view.addSubview(scroll)
        ViewWidth.constant = self.view.frame.width
        Date.text = date1
        subject.text = subject1
        MessageContent.text = message1
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        compose.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        compose.setTitle("Reply", for: UIControl.State())
        compose.tintColor = UIColor.white
        compose.addTarget(self, action: #selector(SaveButtonClicked(_:)), for: .touchUpInside)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
//    override func viewWillDisappear(_ animated: Bool) {
//      self.removeFromParentViewController()
//    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func SaveButtonClicked(_ sender:UIButton)
    {
        let stoy = UIStoryboard(name: "Main", bundle: nil)
        let viewControl = stoy.instantiateViewController(withIdentifier: "MessageViewController") as! ced_MessageViewController
        // viewControl.isCustomer = true
        self.navigationController?.pushViewController(viewControl, animated: true)
        return
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

