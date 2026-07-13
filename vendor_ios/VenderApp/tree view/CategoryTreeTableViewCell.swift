//
//  TreeTableViewCell.swift
//  RATreeViewExamples
//
//  Created by Rafal Augustyniak on 22/11/15.
//  Copyright © 2015 com.Augustyniak. All rights reserved.
//

import UIKit
import SDWebImage
class CategoryTreeTableViewCell : UITableViewCell {

    
    
    @IBOutlet weak var rightImage: UIImageView!
    
    
    @IBOutlet private weak var detailsLabel: UILabel!
    @IBOutlet private weak var customTitleLabel: UILabel!

    @IBOutlet weak var img: UIImageView!
    

    override func awakeFromNib() {
        selectedBackgroundView? = UIView()
        selectedBackgroundView?.backgroundColor = .clear
        if(ced_storeVC.selectLangauge == "ar"){
            setArabicView()
        }
    }
    
    func setArabicView(){
        self.transform = CGAffineTransformMakeScale(-1.0, 1.0)
        rightImage.transform = CGAffineTransformMakeScale(-1.0, 1.0)
        img.transform = CGAffineTransformMakeScale(-1.0, 1.0)
        detailsLabel.transform = CGAffineTransformMakeScale(-1.0, 1.0)
        customTitleLabel.transform = CGAffineTransformMakeScale(-1.0, 1.0)
        detailsLabel.textAlignment = .right
        customTitleLabel.textAlignment = .right
    }

    var additionButtonActionBlock : ((CategoryTreeTableViewCell) -> Void)?;
    @objc func setup(withTitle title: String, detailsText: String, level : Int, additionalButtonHidden: Bool, img: String) {
        customTitleLabel.text = title
        detailsLabel.text = ""
        
        self.img.image=UIImage(named: img)
        //self.img.sd_setImage(with: URL(string: img), placeholderImage: UIImage(named: "bullet"))
        var left = 11.0 + 20.0 * CGFloat(level)
        if(title.lowercased() == "Default Category".lowercased() || title.lowercased() == "صنف المنتج".lowercased()){
            left = 0
            self.customTitleLabel.frame.origin.x = 15
        }else{
            self.customTitleLabel.frame.origin.x = left+30
            self.img.frame.origin.x = left
            self.detailsLabel.frame.origin.x = left+30
        }
        if(ced_storeVC.selectLangauge == "ar"){
            detailsLabel.textAlignment = .right
            customTitleLabel.textAlignment = .right
        }
    }

    @objc func additionButtonTapped(_ sender : AnyObject) -> Void {
        /*if let action = additionButtonActionBlock {
            action(self)
        }*/
    }

}
