//
//  sendInvitationViewController.swift
//  VenderApp
//
//  Created by MacMini on 24/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class sendInvitationViewController: ced_VendorBaseClass {

    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var sendButton: UIButton!
    
    @IBOutlet weak var scrollsView: UIScrollView!
    
    @IBOutlet weak var superView: UIView!
    
    
    @IBOutlet weak var superViewWidth: NSLayoutConstraint!
    
    @IBOutlet weak var superViewHeight: NSLayoutConstraint!
    
    
    @IBOutlet weak var stackView: UIStackView!
    
    @IBOutlet weak var stackViewHeight: NSLayoutConstraint!
    
    @IBOutlet weak var addEmailButton: UIButton!
    var givenTag=0
    override func viewDidLoad() {
        super.viewDidLoad()

        var colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topLabel.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        sendButton.addTarget(self, action: #selector(sendButtonPressed(_:)), for: .touchUpInside)
        superViewWidth.constant=scrollsView.frame.width
        stackViewHeight.constant=180
        superViewHeight.constant=260
        addEmailButton.addTarget(self, action: #selector(addEmailButtonPressed(_:)), for: .touchUpInside)
        let invitationEmailView=invitationView()
        invitationEmailView.cardView()
        invitationEmailView.removeButton.isHidden=true
        invitationEmailView.frame=CGRect(x: 0, y: 0, width: scrollsView.frame.width, height: 180)
        stackView.distribution = .equalSpacing
        stackView.spacing=10
        stackView.addArrangedSubview(invitationEmailView)
        // Do any additional setup after loading the view.
    }

    
    @objc func sendButtonPressed(_ sender: UIButton){
        
        var msgArray="[\""
        var emailArray="[\""
        for i in 0..<stackView.arrangedSubviews.count{
            let itm=stackView.arrangedSubviews[i] as! invitationView
            if i==stackView.arrangedSubviews.count-1{
                msgArray+=itm.email.text!+"\"]"
                emailArray+=itm.msg.text!+"\"]"
            }
            else
            {
                msgArray+=itm.email.text!+"\",\""
                emailArray+=itm.msg.text!+"\",\""
            }
            
            
        }
        
        print(msgArray)
        print(emailArray)
        
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&email=\(emailArray)&msg=\(msgArray)"
        
        self.sendRequest(url: "vsubaccountapi/customer/send", parameters: postString)
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if requestUrl=="vsubaccountapi/customer/send"{
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if json["data"]["status"].stringValue=="true"{
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: json["data"]["message"].stringValue)
                }
            }
            
        }
    }
    @objc func addEmailButtonPressed(_ sender: UIButton){
        let invitationEmailView=invitationView()
        invitationEmailView.cardView()
        invitationEmailView.removeButton.addTarget(self, action: #selector(removeButtonPressed(_:)), for: .touchUpInside)
        invitationEmailView.tag=givenTag
        invitationEmailView.removeButton.tag=givenTag
        givenTag+=1
        invitationEmailView.frame=CGRect(x: 0, y: 0, width: stackView.frame.width, height: 180)
        stackViewHeight.constant+=190
        superViewHeight.constant+=190
        stackView.addArrangedSubview(invitationEmailView)
    }
    @objc func removeButtonPressed(_ sender: UIButton){
        for i in 1..<stackView.arrangedSubviews.count{
            let itm=stackView.arrangedSubviews[i] as! invitationView
            
            if itm.removeButton==sender{
                stackView.arrangedSubviews[i].removeFromSuperview()
                stackViewHeight.constant-=190
                superViewHeight.constant-=190
            }
        }
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
