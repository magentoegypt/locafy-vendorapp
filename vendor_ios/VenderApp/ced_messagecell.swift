//
//  ced_messagecell.swift
//  VenderApp
//
//  Created by Macmini on 15/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_messagecell: UITableViewCell {
    
    
    @IBOutlet weak var QuoteTotalsLable: UILabel!
    @IBOutlet weak var QuoteTotals: UIView!
    
    @IBOutlet weak var ced_messagecell: UIView!
    
    @IBOutlet weak var SendMessage: UIView!
    
    @IBOutlet weak var ced_messagecell_messages: UIView!
    
    @IBOutlet weak var chathistory: UILabel!
    
    @IBOutlet weak var messagesection: UILabel!
    
    @IBOutlet weak var cellview: UIView!
    
    
    @IBOutlet weak var viewmessage: UIButton!
    
    
    @IBOutlet weak var datamessage: UILabel!
    
    @IBOutlet weak var messagedata: UILabel!
    
    @IBOutlet weak var Subtotal: UITextField!
    
   
    @IBOutlet weak var ShippingHandling: UITextField!
    @IBOutlet weak var GrandTotal: UITextField!
    @IBOutlet weak var TotalDue: UITextField!
    @IBOutlet weak var Status: UIButton!
    @IBOutlet weak var SAVEQUOTE: UIButton!
    @IBOutlet weak var messagetext: UITextView!
    @IBOutlet weak var sendmessage: UIButton!
    
    
    @IBOutlet weak var subtotalLbl: UILabel!
    @IBOutlet weak var grandTotalLbl: UILabel!
    @IBOutlet weak var totalDueLbl: UILabel!
    @IBOutlet weak var shippHandleLbl: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
