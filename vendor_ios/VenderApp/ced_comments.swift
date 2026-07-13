//
//  ced_comments.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 01/07/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_comments: UITableViewCell {

    @IBOutlet weak var t1: UILabel!
    @IBOutlet weak var t2: UILabel!
    @IBOutlet weak var t3: UILabel!
    @IBOutlet weak var commentsDate: UILabel!
    @IBOutlet weak var userNotified: UILabel!
    @IBOutlet weak var comment: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var commenttextView: UITextView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        if(commenttextView != nil){
        commenttextView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        }
      
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
