//
//  ced_creditmemocell.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 30/06/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_creditmemocell: UITableViewCell {

    @IBOutlet weak var t1: UILabel!
    @IBOutlet weak var t2: UILabel!
    @IBOutlet weak var t3: UILabel!
    @IBOutlet weak var t4: UILabel!
    @IBOutlet weak var t5: UILabel!
    @IBOutlet weak var t6: UILabel!
    @IBOutlet weak var t7: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var creditmemo: UILabel!
    @IBOutlet weak var createdAt: UILabel!
    @IBOutlet weak var orderId: UILabel!
    @IBOutlet weak var orderDate: UILabel!
    @IBOutlet weak var billtoName: UILabel!
    @IBOutlet weak var status: UILabel!
    @IBOutlet weak var refunded: UILabel!
    
    @IBOutlet weak var containerView1: UIView!
    
    @IBOutlet weak var containerView2: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
        containerView1.setThemeColor()
        containerView2.setThemeColor()
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }

}
