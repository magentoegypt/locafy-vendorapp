//
//  ced_CmsBlockTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_CmsBlockTableViewCell: UITableViewCell {

    @IBOutlet weak var addCmsBlock: UIButton!
    @IBOutlet weak var LayoutContent: NSLayoutConstraint!
    @IBOutlet weak var Layout: UILabel!
    @IBOutlet weak var LayoutHeigth: NSLayoutConstraint!
    @IBOutlet weak var Containerview: UIView!
    @IBOutlet weak var lastModified: UILabel!
    @IBOutlet weak var created: UILabel!
    @IBOutlet weak var urlKey: UILabel!
    @IBOutlet weak var id: UILabel!
    @IBOutlet weak var status: UILabel!
    @IBOutlet weak var approved: UILabel!
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var DeleteButton: UIButton!
    @IBOutlet weak var EditButton: UIButton!
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var view: TwoButtonView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
