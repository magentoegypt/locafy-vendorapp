//
//  cedPurchaseImageCell.swift
//  VenderApp
//
//  Created by cedcoss on 16/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedPurchaseImageCell: UITableViewCell, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {

    var imageArray = [String]();
    @IBOutlet weak var mainCollectionView: UICollectionView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    @objc func reloadData()
    {
        mainCollectionView.delegate = self;
        mainCollectionView.dataSource = self;
    }
    
    @objc func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1;
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return imageArray.count;
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cedPurchaseImageCollectionCell", for: indexPath) as? cedPurchaseImageCollectionCell
        {
            cell.purchaseImageView.sd_setImage(with: URL(string: imageArray[indexPath.row]), placeholderImage: UIImage(named: "placeholder"))
            return cell;
        }
        return UICollectionViewCell();
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.width/2 - 10, height: collectionView.frame.height)
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
