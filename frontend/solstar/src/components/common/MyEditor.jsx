import { useState, useRef } from 'react';
import ReactQuill from 'react-quill-new';
import 'react-quill/dist/quill.snow.css';
import './MyEditor.css';

function MyEditor() {
  const [editorValue, setEditorValue] = useState('');
  const editorRef = useRef(null);

  const handleChange = (value) => {
    setEditorValue(value);
  };

  const handleImageUpload = (file, callback) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const base64 = e.target.result;
      callback(base64, '이미지 설명');
    };
    reader.readAsDataURL(file);
  };

  const modules = {
    toolbar: [
      [{ header: '1' }, { header: '2' }],
      ['bold', 'underline'],
      ['image'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      ['link'],
      [{ align: [] }],
    ],
    clipboard: {
      matchVisual: false,
    },
  };

  return (
    <div className="react-quill-container">
      <ReactQuill
        ref={editorRef}
        value={editorValue}
        onChange={handleChange}
        theme="snow"
        placeholder="내용을 입력하세요..."
        modules={modules}
        formats={[
          'header',
          'list',
          'bold',
          'italic',
          'underline',
          'link',
          'image',
          'align',
        ]}
        // 이미지 업로드 핸들러를 hooks를 통해 추가
        hooks={{
          addImageBlobHook: handleImageUpload,
        }}
      />
    </div>
  );
}

export default MyEditor;
