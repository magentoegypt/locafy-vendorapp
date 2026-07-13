//
//  JNCountryPickerViewController+UISearchBarDelegate.swift
//  CountryPicker
//
//  Created by Hamzawy Khanfar on 03/10/2019.
//  Copyright © 2019 Hamzawy Khanfar. All rights reserved.
//

import UIKit

// MARK: - UISearchBarDelegate
extension JNCountryPickerViewController: UISearchBarDelegate {
    
    /**
     Search Button clicked
     */
    public func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        
        // Scroll to top
        self.tableView.scrollToRow(at: IndexPath(row: 0, section: 0), at: .top, animated: false)
        
        // Filter with search text
        self.viewModel.filterContent(text: searchBar.text ?? "")
        
        // Reload data after filtering
        self.tableView.reloadData()
        
        // End searching and hide keyboard
        searchBar.resignFirstResponder()
    }
    
    /**
    Search text changed
     */
    public func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
        if searchText.isEmpty {
            // Scroll to top
            self.tableView.scrollToRow(at: IndexPath(row: 0, section: 0), at: .top, animated: false)
            
            // Filter with search text
            self.viewModel.filterContent(text: "")
            
            // Reload data after filtering
            self.tableView.reloadData()
        }
    }
    
    public func  searchBar(_ searchBar: UISearchBar,shouldChangeTextIn range: NSRange,replacementText text: String
    ) -> Bool{
        let searchText = searchBar.text ?? ""
            if text.isEmpty {
                self.viewModel.filterContent(text: String(searchText.dropLast()))
            } else {
                self.viewModel.filterContent(text: searchText + text)
            }
        self.tableView.reloadData()
        return true
    }
    
}
