import propTypes from 'prop-types';

import { useEffect, useRef } from 'react';
import ReactQuill from 'react-quill-new';
import 'react-quill-new/dist/quill.snow.css';
import './MyEditor.css';

function MyEditor({ funding, onChange, setRegistActive }) {
  const editorRef = useRef(null);

  useEffect(() => {
    const isEmptyContent =
      !funding.content || funding.content === '<p><br></p>';
    setRegistActive(!isEmptyContent);
    console.log(funding.content);
  }, [funding.content]);

  const handleChange = (value) => {
    // content 값이 변경될 때 onChange를 호출하여 funding 객체를 업데이트합니다.
    onChange({ target: { id: 'content', value } });
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
        value={funding.content || ''}
        onChange={handleChange}
        theme="snow"
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
        hooks={{
          addImageBlobHook: handleImageUpload,
        }}
        style={{ height: '350px' }}
      />
    </div>
  );
}

MyEditor.propTypes = {
  funding: propTypes.object.isRequired,
  onChange: propTypes.func.isRequired,
  setRegistActive: propTypes.func.isRequired,
};

export default MyEditor;
