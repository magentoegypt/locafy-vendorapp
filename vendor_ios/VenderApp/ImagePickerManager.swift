//
//  ImagePickerManager.swift
//  LocafyApp
//
//  Created by Apple on 10/04/2025.
//  Copyright © 2025 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ImagePickerManager: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    private var pickerCallback: ((UIImage?) -> Void)?
    private weak var presentingVC: UIViewController?

    func presentImagePicker(from viewController: UIViewController, sourceType: UIImagePickerController.SourceType, callback: @escaping (UIImage?) -> Void) {
        guard UIImagePickerController.isSourceTypeAvailable(sourceType) else {
            callback(nil)
            return
        }

        presentingVC = viewController
        pickerCallback = callback

        let picker = UIImagePickerController()
        picker.delegate = self
        picker.sourceType = sourceType
        picker.allowsEditing = true

        viewController.present(picker, animated: true, completion: nil)
    }

    // MARK: - UIImagePickerControllerDelegate

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        let image = (info[.editedImage] ?? info[.originalImage]) as? UIImage
        picker.dismiss(animated: true) {
            self.pickerCallback?(image)
        }
    }

    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true) {
            self.pickerCallback?(nil)
        }
    }
}
