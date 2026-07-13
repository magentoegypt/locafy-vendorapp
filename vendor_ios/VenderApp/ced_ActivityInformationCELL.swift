//
//  ced_ActivityInformationCELL.swift
//  VenderApp
//
//  Created by Macmini on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ActivityInformationCELL: UITableViewCell {
    
    
    @IBOutlet weak var toplable: UILabel!
    
    @IBOutlet weak var toplableheight: NSLayoutConstraint!
    
    @IBOutlet weak var status: UILabel!
    @IBOutlet weak var date: UILabel!
    @IBOutlet weak var notificationheight: NSLayoutConstraint!
    
    
    @IBOutlet weak var cellview: UIView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        cellview.makeCard(cellview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
