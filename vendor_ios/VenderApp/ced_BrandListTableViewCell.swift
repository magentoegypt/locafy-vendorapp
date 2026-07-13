//
//  ced_BrandListTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 24/02/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_BrandListTableViewCell: UITableViewCell {

    
    @IBOutlet weak var ContainerView: UIView!
    @IBOutlet weak var twoButtons: TwoButtonView!
    @IBOutlet weak var ImageLogo: UIImageView!
    
    @IBOutlet weak var Thumbnail: UILabel!
    
    @IBOutlet weak var BrandName: UILabel!
    @IBOutlet weak var ImageThumbnail: UIImageView!
    
    @IBOutlet weak var creation: UILabel!
    
    @IBOutlet weak var brandId: UILabel!
    
    
    @IBOutlet weak var logoText: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
