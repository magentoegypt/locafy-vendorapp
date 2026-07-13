//
//  ced_vendorLoginViewCell.swift
//  VenderApp
//
//  Created by Saumya Kashyap on 17/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_vendorLoginViewCell: UITableViewCell {

    
    @IBOutlet weak var loginContainerView: UIView!
    @IBOutlet weak var forgotPassword: UIButton!
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var regEmail: UITextField!
    @IBOutlet weak var phoneNumber: UITextField!
    
    @IBOutlet weak var countryView: UIView!
    @IBOutlet weak var countrySelect: UIButton!
    
    @IBOutlet weak var startSellingButton: UIButton!
    @IBOutlet weak var alreadyCustomerButton: UIButton!
    @IBOutlet weak var storyStartSelling: UIButton!
   // @IBOutlet weak var videoWebView: UIWebView!
    @IBOutlet weak var buyNow: UIButton!
    @IBOutlet weak var ourStoryView: UIView!
    @IBOutlet weak var mainView: UIView!
    
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        loginContainerView.makeCard(loginContainerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        loginButton.backgroundColor=UIColor(red:0.91, green:0.11, blue:0.22, alpha:1.0)
        startSellingButton.backgroundColor=UIColor(red:0.91, green:0.11, blue:0.22, alpha:1.0)
        startSellingButton.layer.cornerRadius=CGFloat(5)
        mainView.backgroundColor=UIColor(red:0.91, green:0.11, blue:0.22, alpha:1.0)
      //  ourStoryView.backgroundColor=UIColor(red:0.91, green:0.11, blue:0.22, alpha:1.0)
        
        countryView.layer.cornerRadius=CGFloat(5)
        countrySelect.layer.cornerRadius=CGFloat(5)
        
        
        
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
